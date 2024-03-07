

/**
 * @description: 命名转换
 * @author: JINZEPENG
 * @create: 2021/10/15 16:02
 **/
public class NameDealUtil {

    enum NameStatus{
        sql,ori,used
    }

    static class Name{
        private NameStatus status;
        private String nameStr;
        private char[] nameChars;

        public Name(String nameStr) {
            setNameStr(nameStr);
        }

        public String getNameStr() {
            return nameStr;
        }

        public void setNameStr(String nameStr) {
            this.nameStr = nameStr;
            if (nameStr==null)return;
            nameChars = this.nameStr.toCharArray();
            if (this.nameStr.contains("_")){
                this.status=NameStatus.sql;
            }else if (nameChars[0]>='A' && nameChars[0]<='Z'){
                this.status=NameStatus.ori;
            }else {
                this.status=NameStatus.used;
            }
        }

        public String returnSqlName(){
            if (this.status==NameStatus.sql){
                return this.nameStr;
            }else if (this.status==NameStatus.ori){
                StringBuilder ans = new StringBuilder();
                for (int i=0;i<this.nameChars.length;i++){
                    char c = this.nameChars[i];
                    if (i==0){
                        ans.append((c+"").toLowerCase());
                    }else {
                        if (c>='A'&&c<='Z'){
                            ans.append("_").append((char)('a'+c-'A'));
                        }else {
                            ans.append(c);
                        }
                    }
                }
                return ans.toString();
            }else{
                setNameStr(returnOriginName());
                return returnSqlName();
            }
        }

        public String returnOriginName(){
            if (this.status==NameStatus.ori){
                return this.nameStr;
            }else if (this.status==NameStatus.sql){
                StringBuilder ans = new StringBuilder();
                for (int i=0;i<this.nameChars.length;i++){
                    char c = this.nameChars[i];
                    if (i==0){
                        ans.append((c+"").toUpperCase());
                    }else {
                        if (c=='_'){
                            ans.append((this.nameChars[++i]+"").toUpperCase());
                        }else {
                            ans.append(c);
                        }
                    }
                }
                return ans.toString();
            }else {
                return (this.nameChars[0] + "").toUpperCase() + this.nameStr.substring(1);
            }
        }

        public String returnUseName(){
            if (this.status==NameStatus.used){
                return this.nameStr;
            }else if (this.status==NameStatus.ori){
                return (this.nameChars[0] + "").toLowerCase() + this.nameStr.substring(1);
            }else {
                setNameStr(returnOriginName());
                return returnUseName();
            }
        }

    }

    public static String generatorSet(String[] strings, String className){
        StringBuilder sb = new StringBuilder();
        for (String s:strings){
            if (s==null||s.length()==0)continue;
            Name name = new Name(s);
            String tmp = className+".set"+name.returnOriginName()+"("+name.returnUseName()+");\n";
            sb.append(tmp);
        }
        return sb.toString();
    }

    public static String generatorGet(String[] strings, String className){
        StringBuilder sb = new StringBuilder();
        for (String s:strings){
            if (s==null||s.length()==0)continue;
            Name name = new Name(s);
            String tmp = "String "+name.returnUseName()+" = "+className+".get"+name.returnOriginName()+"();\n";
            sb.append(tmp);
        }
        return sb.toString();
    }

    public static String appendString(String[] strings){
        StringBuilder sb = new StringBuilder();
        for (String s:strings){
            if (s==null||s.length()==0)continue;
            Name name = new Name(s);
            String tmp = ".append("+name.returnUseName()+").append(\",\")";
            sb.append(tmp);
        }
        return sb.toString();
    }


    public void test1(){
        Name name = new Name("p_acct_issr_id");
        System.out.println(name.returnUseName());
        System.out.println(name.returnOriginName());
        System.out.println(name.returnSqlName());


        name = new Name("PAcctIssrId");
        System.out.println(name.returnUseName());
        System.out.println(name.returnOriginName());
        System.out.println(name.returnSqlName());

        name = new Name("pAcctIssrId");
        System.out.println(name.returnUseName());
        System.out.println(name.returnOriginName());
        System.out.println(name.returnSqlName());

    }


    public void test2(){
        String txt = "id,bill_date,btch_id,pyer_acct_type,pyee_acct_type,trx_id,trx_amt,trx_amt_int,trx_ctgy,trx_status,accept_org_issr_id,pyer_acct_issr_id,pyer_bank_trx_id,pyer_settle_bank,pyee_acct_issr_id,pyee_bank_trx_id,pyee_settle_bank,acct_tp,trx_inf,biz_tp,fee_mdl_cd,fee_count,fee_inf,bill_result";

        String[] strings = txt.split(",");
//        String epccWithdrawApplication = generatorSet(strings, "epccWithdrawApplication");
//        String billDetail = generatorGet(strings, "billDetail");
        String billDetail = appendString(strings);
        System.out.println(billDetail);
    }







}
