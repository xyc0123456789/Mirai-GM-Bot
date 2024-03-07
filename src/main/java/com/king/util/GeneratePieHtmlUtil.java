package com.king.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @description: 饼图
 * @author: JINZEPENG
 * @create: 2023/5/23 13:28
 **/
@Slf4j
public class GeneratePieHtmlUtil {

    public static final String EChartsJS;

    static {
        InputStream resourceAsStream = GeneratePieHtmlUtil.class.getClassLoader().getResourceAsStream("static/echarts.5.2.1.min.js");
        ByteArrayOutputStream outputStream = StreamUtil.inputStreamToOutputStream(resourceAsStream);
        try {
            EChartsJS = outputStream.toString(String.valueOf(StandardCharsets.UTF_8));
            System.setProperty("webdriver.chrome.driver", "/home/appuser/mirai_workplace/driver/chromedriver");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("init err", e);
            throw e;
        }
    }

    private static final String html = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "  <head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <title>My ECharts Page</title>\n" +
            "    <style>\n" +
//                "      @font-face {\n" +
//                "        font-family: 'MyFont';\n" +
//                "        src: url('/home/appuser/mirai_workplace/font/sarasa-mono-sc-regular.ttf');\n" +
//                "      }\n" +
            "      body {\n" +
            "        font-family: 'FZXS14', 'Noto Serif SC', '宋体';\n" +
            "        width: 95vw; /* 宽度占据整个可视窗口 */\n" +
            "        height: 95vh; /* 高度占据整个可视窗口 */\n" +
            "        position: absolute; /* 设置为绝对定位 */\n" +
            "        top: 10; /* 距离顶部的距离为0 */\n" +
            "        left: 10; /* 距离左侧的距离为0 */\n" +
//            "        font-size: 1vw;; /* 距离左侧的距离为0 */\n" +
            "      }\n" +
            "    </style>" +
            "    <!-- 引入 echarts.js -->\n" +
            "    <script>"+EChartsJS+"</script>\n" +
            "  </head>\n" +
            "  <body>\n" +
            "  <div id=\"main\" style=\"width: 3600px;height:1080px;\"></div>\n" +
            "    <script type=\"text/javascript\">\n" +
            "      // 基于准备好的dom，初始化echarts实例\n" +
            "      var myChart = echarts.init(document.getElementById('main'));\n" +
            "\n" +
            "   const data = ${data};\n" +
            "option = {\n" +
            "  title: {\n" +
            "    text: '${title}',\n" +
            "    subtext: '${subTitle}',\n" +
            "    left: 'center',\n" +
            "    textStyle: {\n" +
            "      fontSize: 50 // 改变标题字体大小为 18px\n" +
            "    },\n"+
            "    subtextStyle: {\n" +
            "      fontSize: 40 // 改变标题字体大小为 18px\n" +
            "    }\n"+
            "  },\n" +
            "  tooltip: {\n" +
            "    trigger: 'item',\n" +
            "    formatter: '{a} <br/>{b} : {c} ({d}%)'\n" +
            "  },\n" +
            "  legend: {\n" +
            "    type: 'scroll',\n" +
            "    orient: 'vertical',\n" +
            "    left: 10,\n" +
            "    top: 20,\n" +
            "    bottom: 20,\n" +
            "    data: data.legendData,\n" +
            "    textStyle: {\n" +
            "      fontSize: 30 // 图例字体大小为 14px\n" +
            "    }"+
            "  },\n" +
            "  series: [\n" +
            "    {\n" +
            "      name: '姓名',\n" +
            "      type: 'pie',\n" +
            "      radius: '55%',\n" +
            "      center: ['66%', '60%'],\n" +
            "      data: data.seriesData,\n" +
            "      label: {\n" +
            "        fontSize: 30 // 改变标签字体大小为 14px\n" +
            "      }," +
            "      emphasis: {\n" +
            "        itemStyle: {\n" +
            "          shadowBlur: 10,\n" +
            "          shadowOffsetX: 0,\n" +
            "          shadowColor: 'rgba(0, 0, 0, 0.5)'\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "};\n" +
            "myChart.setOption(option);\n" +
            "    </script>\n" +
            "  </body>\n" +
            "</html>\n";
    public static final String DATA = "\\$\\{data\\}";
    public static final String TITLE = "\\$\\{title\\}";
    public static final String SUB_TITLE = "\\$\\{subTitle\\}";
//    data
//      legendData
//          [String]
//      seriesData
//          [{
//          name: str,
//          value: num
//        }]

    public static String constructJsonData(Map<String, String> map){
        Map<String,Object> data = new HashMap<>();
        ArrayList<Object> legendData = new ArrayList<>();
        ArrayList<Object> seriesDatas = new ArrayList<>();
        // 创建一个字符串数组

        // 循环遍历legendData数组，将每个元素添加到JSON数组中
        for (String legend : map.keySet()) {
            String valueStr = map.get(legend);
            double value = Double.parseDouble(valueStr);
            if (value<0.00005){
                continue;
            }
            Map<String,Object> seriesData = new HashMap<>();
            seriesData.put("name", legend+":"+ valueStr);
            seriesData.put("value", value);
            seriesDatas.add(seriesData);
            legendData.add(legend+":"+ valueStr);
        }

        // 将legendData和seriesData添加到JSON对象中
        data.put("legendData", legendData);
        data.put("seriesData", seriesDatas);

        return JsonUtil.toJson(data);
    }

    private static String generateHtml(Map<String, String> content, String title, String subTitle){
        if (MyStringUtil.isEmpty(title)){
            title = "title";
        }
        if (MyStringUtil.isEmpty(subTitle)){
            subTitle = "subTitle";
        }

        String constructJsonData = constructJsonData(content);
        String replaceAll = html.replaceAll(DATA, constructJsonData);
        replaceAll = replaceAll.replaceAll(TITLE, title);
        replaceAll = replaceAll.replaceAll(SUB_TITLE, subTitle);
        return replaceAll;
    }


    public static File generatePieImg(Map<String, String> content, String title, String subTitle){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            String htmlStr = generateHtml(content, title, subTitle);
            return CommonMarkUtil.fromHtmlFileToFileBySelenium(htmlStr, uuid);
        }catch (Exception e){
            log.error("", e);
        }finally {
            FileUtil.deleteFile(new File(uuid + ".html"));
        }
        return null;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Map<String, String> ori = new TreeMap<>();
        ori.put("a                          lllllllllllllllll", "20.5");
        ori.put("b", "10");
        ori.put("c", "30");
        ori.put("d", "0");

        String s = generateHtml(ori, "test你好", "mytest");
        CommonMarkUtil.fromHtmlFileToFileBySelenium(s, "a");

    }

}

