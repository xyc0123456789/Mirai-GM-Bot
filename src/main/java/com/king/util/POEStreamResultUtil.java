package com.king.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.MDC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

/**
 * @description: SSE
 * @author: xyc0123456789
 * @create: 2023/3/18 15:12
 **/
@Slf4j
public class POEStreamResultUtil {

    public static final AtomicReference<String> lastId= new AtomicReference<>("jajsbgia");

    public static String readEvents(){
        String url = "http://127.0.0.1:8090/events"; // SSE 服务器的 URL
        AtomicReference<String> responseBody=new AtomicReference<>("");
        try {
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(10000) // 设置连接超时时间为10秒
                    .setSocketTimeout(15000) // 设置读取超时时间为30秒
                    .build();
            CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            URIBuilder uriBuilder = new URIBuilder(url);
//            uriBuilder.addParameter("param1", "value1"); // 添加请求参数

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setHeader("Accept", "text/event-stream");

            client.execute(httpGet, response -> {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    log.error("SSE 连接失败：{}", response.getStatusLine());
                }

                response.getEntity().getContent().read(); // 忽略第一个字节
                while (!Thread.currentThread().isInterrupted()) {
                    byte[] buffer = new byte[9000];
                    int read = response.getEntity().getContent().read(buffer);

                    if (read == -1) {
                        log.info("SSE 连接已关闭");
                    }

                    String message = new String(buffer, 0, read, "UTF-8");
//                    log.info("收到消息：{}", message);
                    if (message.startsWith("event: done")||message.startsWith("vent: done")) {
                        boolean flag = false;
                        try {
                            int i = message.indexOf("data:");
                            String dataStr = message.substring(i + 5);
                            Map json = JsonUtil.fromJson(dataStr, Map.class);
                            Map<String, Object> data = (Map<String, Object>) json.get("data");
                            String id = (String) data.get("id");
                            if (!MyStringUtil.isEmpty(id) && !lastId.get().equals(id)) {
                                lastId.getAndSet(id);
                                responseBody.getAndSet(dataStr);
                                log.error("end:" + message);
                                flag = true;
                            }
                        } catch (Exception e) {
                        }
                        if (flag) {
                            throw new IOException("");
                        }
                    }
                }
                return null;
            });
        } catch (IOException | URISyntaxException e) {
            if (!(e instanceof IOException) || !MyStringUtil.isEmpty((e).getMessage())){
                if ("Read timed out".equals(e.getMessage())){
                    log.error(e.getMessage());
                }else {
                    log.error("POE client error", e);
                }
            }
        }
        String res = responseBody.get().trim();
//        log.info("responseBody:{}", res);
        return res;
    }

    public static void main(String[] args) {
        log.info(readEvents());
    }


}
