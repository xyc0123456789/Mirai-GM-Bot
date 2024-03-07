package com.king.util;

import com.king.config.CommonConfig;
import com.king.model.DataResponse;
import com.king.model.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RequestForPdf {

    public static final String PDF_PARENT_DIR = CommonConfig.getWorkingDir() + "pdfdownload/";
    private static final Map<String, String> REPLACE_MAP = new HashMap<>();

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService muiltSchedule = Executors.newScheduledThreadPool(5);

    static {
        REPLACE_MAP.put(",", "，");
        REPLACE_MAP.put(":", "：");
        REPLACE_MAP.put("\\?", "？");
    }

    public static DataResponse<Map<String, String>> normalDownload(String message) {
        try {
            Map<String, String> urlAndName = new HashMap<>();
            String[] rows = message.split("\n");
            int index = 0;
            while (index < rows.length) {
                String row = rows[index].trim();
                int bIndex = row.indexOf("】");
                if ((bIndex != -1 && row.startsWith("【")) || row.startsWith("name:") || row.startsWith("filename:")) {
                    String title;
                    if (bIndex != -1) {
                        title = row.substring(bIndex + 1).trim();
                    } else {
                        title = row.substring(row.indexOf(":") + 1).trim();
                    }
                    title = dealFileName(title);
                    String url = "";
                    boolean findUrl = true;
                    while (findUrl) {
                        index++;
                        if (index >= rows.length) {
                            break;
                        }
                        row = rows[index].trim();
                        int httpIndex = row.indexOf("http");
                        if (row.startsWith("链接") || httpIndex != -1) {
                            findUrl = false;
                            url = row.substring(httpIndex);
                        }
                    }
                    if (url.length() > 0) {
                        urlAndName.put(url, title);
                    }
                }
                index++;
            }
            String parent = PDF_PARENT_DIR + DateFormateUtil.getCurYYYYMMDDTHHMMSS();
            if (urlAndName.isEmpty()) {
                throw new RuntimeException("message 无效");
            }
            for (String key : urlAndName.keySet()) {
                log.info("url:{}\tpath:{}", key, urlAndName.get(key));
                String filePath = parent + "/" + urlAndName.get(key);
                muiltSchedule.execute(new DownJob(key, filePath, muiltSchedule, 0, null));
            }
            Map<String, String> data = new HashMap<>();
            data.put("filePath", parent);
            data.put("count", String.valueOf(urlAndName.size()));
            log.info("{} :{}", parent, data);
            return new DataResponse<>(Response.ok(), data);
        } catch (Exception e) {
            log.error("dealDownloadMessage err", e);
            return new DataResponse<>(Response.fail(e.getMessage()));
        }
    }


    public static DataResponse<Map<String, String>> dealDownloadMessage(String message) {
        try {
            Map<String, String> urlAndName = new HashMap<>();
            String[] rows = message.split("\n");
            int index = 0;
            while (index < rows.length) {
                String row = rows[index].trim();
                int bIndex = row.indexOf("】");
                if (bIndex != -1 && row.startsWith("【")) {
                    String title = dealFileName(row.substring(bIndex + 1).trim()) + ".pdf";
                    String url = "";
                    boolean findUrl = true;
                    while (findUrl) {
                        index++;
                        if (index >= rows.length) {
                            break;
                        }
                        row = rows[index].trim();
                        int httpIndex = row.indexOf("http");
                        if (row.startsWith("链接") || httpIndex != -1) {
                            findUrl = false;
                            url = dealUrl(row.substring(httpIndex));
                        }
                    }
                    if (url.length() > 0) {
                        urlAndName.put(url, title);
                    }
                }
                index++;
            }
            String parent = PDF_PARENT_DIR + DateFormateUtil.getCurYYYYMMDDTHHMMSS();
            if (urlAndName.isEmpty()) {
                throw new RuntimeException("message 无效");
            }
            for (String key : urlAndName.keySet()) {
                log.info("url:{}\tpath:{}", key, urlAndName.get(key));
                String filePath = parent + "/" + urlAndName.get(key);
                Map<String, String> head = new HashMap<>();
                head.put("referer", "https://arxiv.org");
                head.put("Host", "arxiv.org");
                head.put("Cookie", "browser=101.88.138.1.1646893614589671");
                scheduledExecutorService.execute(new DownJob(key, filePath, scheduledExecutorService, 0, head));
            }
            Map<String, String> data = new HashMap<>();
            data.put("filePath", parent);
            data.put("count", String.valueOf(urlAndName.size()));
            log.info("{} :{}", parent, data);
            return new DataResponse<>(Response.ok(), data);
        } catch (Exception e) {
            log.error("dealDownloadPDFMessage err", e);
            return new DataResponse<>(Response.fail(e.getMessage()));
        }
    }

    public static final String filePath = "filePath";
    public static final String count = "count";

    //https://arxiv.org/pdf/2301.04937.pdf#pdfjs.action=download
    //https://arxiv.org/abs/2301.04937
    public static String dealUrl(String url) {
        return url.replaceAll("abs", "pdf") + ".pdf#pdfjs.action=download";
    }

    public static String dealFileName(String fileName) {
        for (String key : REPLACE_MAP.keySet()) {
            fileName = fileName.replaceAll(key, REPLACE_MAP.get(key));
        }
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "");
    }


    private static Response download(String url, String filePath, Map<String, String> head) throws Exception {
        if (head == null) {
            head = new HashMap<>();
        }
        head.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
        HttpClient httpClient = new HttpClient(url, head);
        log.info(url + " started");
        int status = httpClient.sendGetGetOutPutStream("UTF-8", "127.0.0.1:7078");
        if (status == 200) {
            ByteArrayOutputStream outputStream = httpClient.getOutputStream();
            File file = new File(filePath);

            if (!file.getParentFile().exists()) {
                boolean mkdirs = file.getParentFile().mkdirs();
                if (!mkdirs) {
                    log.error("mkdirs failed");
                    return Response.fail("mkdirs failed");
                }
            }

            FileUtil.outPutToFile(outputStream.toByteArray(), file);
            return Response.ok(file.getAbsolutePath());
        }
        return Response.fail(String.valueOf(status));
    }

    static class DownJob implements Runnable {

        private final String url;
        private final String filePath;
        private final ScheduledExecutorService scheduledExecutorService;

        private final Map<String, String> head;

        private long count;

        public DownJob(String url, String filePath, ScheduledExecutorService scheduledExecutorService, long count, Map<String, String> head) {
            this.url = url;
            this.filePath = filePath;
            this.scheduledExecutorService = scheduledExecutorService;
            this.count = count;
            this.head = head;
        }

        @Override
        public void run() {
            try {
                Response response = download(url, filePath, head);
                if (!response.isOk()) {
                    throw new RuntimeException("reset");
                }
                log.info(url + " finished");
            } catch (Exception e) {
                if (!e.getMessage().contains("reset")) {
                    log.error(url + " download failed\t" + filePath, e);
                }
                count += 1;
                long time = (long) (Math.pow(2, count)) * 3;
                if (count < 10) {
                    log.warn("{} will start download after {} seconds", url, time);
                    scheduledExecutorService.schedule(new DownJob(url, filePath, scheduledExecutorService, count, head), time, TimeUnit.SECONDS);
                }
            }
        }
    }

    public static void main(String[] args) {
//        Response response = downloadPdf("https://arxiv.org/pdf/2301.04937.pdf#pdfjs.action=download", "./test.pdf");
//        log.info(response.toString());
//        log.info(dealUrl("https://arxiv.org/abs/2301.04937"));

        String test = "#downpdf\n" +
                "【2】 Head-Free Lightweight Semantic Segmentation with Linear Transformer\n" +
                "标题：基于线性变换的无头轻量级语义切分\n" +
                "链接：https://arxiv.org/abs/2301.04648\n" +
                "\n" +
                "【1】 Data Distillation: A Survey\n" +
                "标题：数据蒸馏：综述\n" +
                "链接：https://arxiv.org/pdf/2301.04272";
//        dealDownloadMessage(test);
        System.out.println(test);
//        System.out.println(test.substring(8).trim());
    }

}
