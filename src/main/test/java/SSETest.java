import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class SSETest {


    public static void main(String[] args) {
        String url = "http://127.0.0.1:8090/events"; // SSE 服务器的 URL

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            URIBuilder uriBuilder = new URIBuilder(url);
//            uriBuilder.addParameter("param1", "value1"); // 添加请求参数

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setHeader("Accept", "text/event-stream");

            String res = client.execute(httpGet, response -> {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    log.error("SSE 连接失败：{}", response.getStatusLine());
                }

                response.getEntity().getContent().read(); // 忽略第一个字节
                String result = "";
                while (!Thread.currentThread().isInterrupted()) {
                    byte[] buffer = new byte[1024];
                    int read = response.getEntity().getContent().read(buffer);

                    if (read == -1) {
                        log.info("SSE 连接已关闭");
                    }

                    String message = new String(buffer, 0, read, "UTF-8");
                    log.info("收到消息：{}", message);
                    if (message.startsWith("event: done")) {
                        log.error("end:" + message);
                    }
                }
                return null;
            });


            log.error("finally:"+res);
        } catch (IOException | URISyntaxException e) {
            log.error("SSE 连接发生异常", e);
        }
    }
}