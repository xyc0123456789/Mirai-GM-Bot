package com.king.util;

/**
 * @description: qrcode
 * @author: JINZEPENG
 * @create: 2023/2/6 15:31
 **/

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.extra.qrcode.BufferedImageLuminanceSource;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.Image;

import javax.imageio.ImageIO;

@Slf4j
public class QrcodeUtil {

    public static String detectQrcode(Image image){
        File file=null;
        try {
            String queryUrl = Image.queryUrl(image);
            file = FileUtil.downLoadImage(queryUrl, "download/image/"+getNameFromUrl(queryUrl)+"."+image.getImageType().toString().toLowerCase());
            return translateQrcode(file);
        }catch (Exception e){
            log.error("detectQrcode err", e);
            return null;
        }finally {
            if (file!=null&&file.exists()){
                file.delete();
            }
        }
    }

    public static String translateQrcode(File file){
        try {
            MultiFormatReader formatReader = new MultiFormatReader();

            BufferedImage image = ImageIO.read(file);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            Result result = formatReader.decode(binaryBitmap, hints);
            log.info("二维码格式类型：" + result.getBarcodeFormat());
            log.info("二维码文本内容：" + result.getText());
            if (result.getBarcodeFormat().equals(BarcodeFormat.QR_CODE)){
                return result.getText();
            }else {
                return null;
            }
        }catch (NotFoundException e){
            log.warn("{} 没有识别到二维码", file.getName());
        }catch (Exception e){
            log.error("translateQrcode err", e);
        }
        return null;
    }



    public static String getNameFromUrl(String url){
        String pattern = "(\\d+-\\d+-[a-zA-Z0-9]+)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(url);
        if (m.find()) {
            return m.group();
        }
        return DateFormateUtil.getCurYYYYMMDDTHHMMSS()+ UUID.randomUUID().toString().replaceAll("-","");
    }


    public static void main(String[] args) throws Exception {

    }
}

