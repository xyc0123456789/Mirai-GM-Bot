package com.king.util;

import gui.ava.html.image.generator.HtmlImageGenerator;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CommonMarkUtil {

    private static String TEXMMLCHTMLJS = "";

    private static final ThreadPoolExecutor DriverQueue = ThreadPoolExecutorUtil.getAQueueExecutor(50);

    static {
        InputStream resourceAsStream = CommonMarkUtil.class.getClassLoader().getResourceAsStream("static/tex-mml-chtml.js");
        ByteArrayOutputStream outputStream = StreamUtil.inputStreamToOutputStream(resourceAsStream);
        try {
            TEXMMLCHTMLJS = outputStream.toString(String.valueOf(StandardCharsets.UTF_8));
            System.setProperty("webdriver.chrome.driver", "/home/appuser/mirai_workplace/driver/chromedriver");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("init err", e);
            throw e;
        }

    }

    public static File createMarkdownImg(String markdownText, int width) {
        return createMarkdownImg(markdownText, false, width);
    }

    public static File createMarkdownImg(String markdownText, boolean math) {
        return createMarkdownImg(markdownText, math, 800);
    }

    public static File createMarkdownImg(String markdownText, boolean math, int width) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            String html = mdToHtmlBypandoc(markdownText, uuid);
            if (html == null){
                throw new Exception("");
            }
            if (math) {
                return generateImageBySelenium(html, uuid);
            } else {
                return convertHtmlToImg(html, uuid, width);
            }
        } catch (Exception e) {
//            log.error("createMarkdownImg err", e);
            try {
                markdownText = markdownText.replaceAll("\n","<br>");
                return convertHtmlToImg(markdownText, uuid, width);
            } catch (Exception ex) {
                log.error("",ex);
                return null;
            }
        } finally {
            deleteFile(uuid + ".md");
            deleteFile(uuid + ".html");
        }
    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        FileUtil.deleteFile(file);
    }

    private static String createHtml(String markdownText) {
//        Parser parser = Parser.builder().build();
//        Node document = parser.parse("# My name is *huhx*");
//        HtmlRenderer renderer = HtmlRenderer.builder().build();
//        String renderString = renderer.render(document);  // <h1>My name is <em>huhx</em></h1>
//        System.out.println(renderString);
        // 创建一个 CommonMark 解析器
        Parser parser = Parser.builder().build();
        // 创建一个 CommonMark HTML 渲染器
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        // 解析 Markdown 文本
        Node document = parser.parse(markdownText);
        // 渲染为 HTML
        return renderer.render(document);
    }

    /**
     * @param markdownText
     * @return uuid
     */
    private static String mdToHtmlBypandoc(String markdownText, String uuid) throws InterruptedException {
//        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        FileUtil.outPutToFile(markdownText, new File(uuid + ".md"));
        String output = ShellExecutorUtil.exe("cd ~/myqqbot&&pandoc " + uuid + ".md -o " + uuid + ".html");
        if (!MyStringUtil.isEmpty(output)) {
            log.warn("pandoc warn: {}", output);
        }
        Thread.sleep(500);
        return FileUtil.readFile(new File(uuid + ".html"));
    }

    public static File convertHtmlToImg(String html, String targetFileUUID) throws Exception {
        return convertHtmlToImg(html, targetFileUUID, 800);
    }

    private static File convertHtmlToImg(String html, String targetFileUUID, int width) throws Exception {
        html = "<!DOCTYPE HTML>  \n" +
                "<html>   \n" +
                "<head> \n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>.</title> \n" +
                "    <style>\n" +
//                "      @font-face {\n" +
//                "        font-family: 'MyFont';\n" +
//                "        src: url('/home/appuser/mirai_workplace/font/sarasa-mono-sc-regular.ttf');\n" +
//                "      }\n" +
                "      body {\n" +
                "        font-family: '宋体';\n" +
                "        margin-top: 10px; /* 设置上边距为20像素 */\n" +
                "        margin-bottom: 30px; /* 设置下边距为30像素 */\n" +
                "        margin-left: 50px; /* 设置左边距为50像素 */\n" +
                "        margin-right: 50px; /* 设置右边距为50像素 */" +
                "      }\n" +
                "    </style>" +
                "</head>    \n" +
                "<body style=\"font-size: 25px;width: " + width + "px\"> " + html + "</body>    \n" +
                "</html>";
        return convertCompletedHtmlToImg(html, targetFileUUID);
    }

    private static File convertCompletedHtmlToImg(String html, String targetFileUUID) throws Exception {
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();

        imageGenerator.loadHtml(html);
        Thread.sleep(100);
        imageGenerator.getBufferedImage();
        Thread.sleep(200);
        imageGenerator.saveAsImage(targetFileUUID + ".png");
        //imageGenerator.saveAsHtmlWithMap("hello-world.html", saveImageLocation);
        //不需要转换位图的，下面三行可以不要
//        BufferedImage sourceImg = ImageIO.read(new File(saveImageLocation));
//        sourceImg = transform_Gray24BitMap(sourceImg);
//        ImageIO.write(sourceImg, "BMP", new File(saveImageLocation));
        return new File(targetFileUUID + ".png");
    }

    public static File generateImageBySelenium(String html, String targetFileUUID) throws ExecutionException, InterruptedException {
//        String oriHtml = FileUtil.readFile(new File(targetFileUUID + ".html"));
        String finalHtml = "<!DOCTYPE HTML>  \n" +
                "<html>   \n" +
                "<head> \n" +
                "    <meta charset=\"utf-8\"> \n" +
                "<script>\n" +
                "MathJax = {\n" +
                "  tex: {\n" +
                "    inlineMath: [['$', '$'], ['\\\\(', '\\\\)']]\n" +
                "  }\n" +
                "};\n" +
                "</script>" +
                "    <script>" + TEXMMLCHTMLJS + "</script> \n" +
//                "    <script src=\"https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js\"></script>\n"+
                "    <title>.</title> \n" +
                "    <style>\n" +
//                "      @font-face {\n" +
//                "        font-family: 'MyFont';\n" +
//                "        src: url('/home/appuser/mirai_workplace/font/sarasa-mono-sc-regular.ttf');\n" +
//                "      }\n" +
                "      body {\n" +
                "        font-family: '宋体';\n" +
                "        width: 95vw; /* 宽度占据整个可视窗口 */\n" +
                "        height: 95vh; /* 高度占据整个可视窗口 */\n" +
                "        position: absolute; /* 设置为绝对定位 */\n" +
                "        top: 10; /* 距离顶部的距离为0 */\n" +
                "        left: 10; /* 距离左侧的距离为0 */\n" +
                "      }\n" +
                "    </style>" +
                "</head>    \n" +
                "<body style=\"font-size: 1rem;\"> " + html + "</body>    \n" +
                "</html>";
        return fromHtmlFileToFileBySelenium(finalHtml, targetFileUUID);
    }


    public static File fromHtmlFileToFileBySelenium(String htmlStr, String uuid) throws ExecutionException, InterruptedException {
        if (MyStringUtil.isEmpty(htmlStr)){
            return null;
        }
        if (MyStringUtil.isEmpty(uuid)){
            uuid = UUID.randomUUID().toString().replaceAll("-", "");
        }
        File htmlFile = new File(uuid + ".html");
        FileUtil.outPutToFile(htmlStr, htmlFile);
        String finalUuid = uuid;
        Future<File> imgFile = DriverQueue.submit(() -> {
            ChromeDriver chromeDriver = null;
            try {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");

                chromeDriver = new ChromeDriver(options);
                chromeDriver.manage().window().maximize();
                Dimension size = new Dimension(1000, 120);
                chromeDriver.manage().window().setSize(size);
                chromeDriver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
                chromeDriver.get("file:///" + htmlFile.getAbsolutePath());


                int windowChromeHeight = (int) (long) chromeDriver.executeScript("return document.body.scrollHeight");
                int windowChromescrollWidth = (int) (long) chromeDriver.executeScript("return document.body.scrollWidth");
                size = new Dimension(windowChromescrollWidth+200, windowChromeHeight+300);
//                log.info(size.getWidth()+"\t"+size.getHeight());
                chromeDriver.executeScript("window.scrollTo(0, 0);");
                chromeDriver.manage().window().setSize(size);

                int maxTimes=5,count = 0;
                String readyState = chromeDriver.executeScript("return document.readyState").toString();
                while (!readyState.equals("complete")) {
                    count++;
                    if (count>maxTimes){
                        break;
                    }
                    log.info("wait 1s");
                    try {Thread.sleep(1000);} catch (InterruptedException ignore) {}
                    readyState = chromeDriver.executeScript("return document.readyState").toString();
                }
                try {Thread.sleep(1000);} catch (InterruptedException ignore) {}

                File screenshotFile = ((TakesScreenshot) chromeDriver).getScreenshotAs(OutputType.FILE);
                // 将图片保存到本地文件
                FileInputStream fileInputStream = new FileInputStream(screenshotFile);
                File img = new File(finalUuid + ".png");
                FileUtil.outPutToFile(StreamUtil.inputStreamToOutputStream(fileInputStream).toByteArray(), img);
                return img;
            } catch (Exception e) {
                log.error("generateImageByHtmlUnit err", e);
            } finally {
                if (chromeDriver != null) {
                    chromeDriver.quit();
                }
            }
            return null;
        });
        return imgFile.get();
    }

    public static void main(String[] args) throws Exception {
        String markdownText = "  <p>这是一段包含 LaTeX 公式的文本：</p>\n" +
                "  <p>\\(f(x) = \\int_{-\\infty}^{\\infty} \\hat f(\\xi)\\,e^{2 \\pi i \\xi x} d\\xi\\)</p>" +
                "<p style=\"font-family: MyFont;\">这是一个段落。</p>" +
                "<p style=\"font-family: 宋体;\">这是一个段落。</p>";
//        System.out.println(createMarkdownImg(markdownText).getAbsolutePath());
//        System.out.println(markdownText);
        //ps -ef | grep chrome | grep -v grep  | awk '{print "kill -9 "$2}'  | sh
        //ps -ef | grep 1937752 | grep -v grep  | awk '{print "kill -9 "$2}'  | sh
        generateImageBySelenium(markdownText, "a");
//        convertHtmlToImg(markdownText, "a");
    }
}
