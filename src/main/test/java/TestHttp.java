import com.king.config.CommonConfig;
import com.king.util.FileUtil;
import com.king.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;

@Slf4j
public class TestHttp {


    public static void main(String[] args) throws Exception {
//        String url="http://127.0.0.1:5000";
//        HttpClient httpClient = new HttpClient(url,30000,30000,false);
//        Map<String,String> stringObjectMap = new HashMap<>();
//        stringObjectMap.put("input","美好的一天开始了");
//        stringObjectMap.put("length","0");
//        int status = httpClient.send(stringObjectMap,StandardCharsets.UTF_8.toString());
//        String result = httpClient.getResult();
//        System.out.println(status);
//        System.out.println(result);

//        File initFile = new File(CommonConfig.WORKPATH+"account.ini");
//        log.info(JsonUtil.toJson(FileUtil.getAccountInfo(initFile)));

        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        int i = resource.getFile().indexOf("/target");
        String s = resource.getFile().substring(0, i + 1) + "src/main/java";
        File file = new File(s);
        System.out.println(s + file.exists());

    }
}
