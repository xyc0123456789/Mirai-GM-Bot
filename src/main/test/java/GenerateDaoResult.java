import org.apache.ibatis.annotations.Result;

public class GenerateDaoResult {

    public static String generate(String string){
        StringBuilder stringBuilder = new StringBuilder();
        String[] split = string.split("\n", -1);
        for (String row:split){
            row = row.trim();
            int dot = row.indexOf(';');
            row = row.substring(0,dot);
            String[] split1 = row.split("\\s+", -1);
            String type = split1[0];
            String name = split1[1];
            NameDealUtil.Name pName = new NameDealUtil.Name(name);
            String sqlName = pName.returnSqlName();
            stringBuilder.append("@Result(property = \"").append(name).append("\",column = \"").append(sqlName)
                    .append("\",javaType = ").append(type).append(".class)").append(",\n");
        }
        return stringBuilder.substring(0,stringBuilder.length()-2);
    }


    public static void main(String[] args) {
        String ori = "Long id;            \n" +
                "Long userId;        \n" +
                "String nick;        \n" +
                "String cardName;    \n" +
                "String titleCard;   \n" +
                "String message;     \n" +
                "String extData;     \n" +
                "LocalDateTime ctime;\n" +
                "LocalDateTime utime;";
        System.out.println(generate(ori));
    }

}
