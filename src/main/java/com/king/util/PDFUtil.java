package com.king.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

/**
 * @description: pdf
 * @author: xyc0123456789
 * @create: 2023/6/26 17:04
 **/
@Slf4j
public class PDFUtil {

    public static String readPdf(File pdfFile) throws RuntimeException{
        try {
            PDDocument document = PDDocument.load(pdfFile);
            // 创建PDFTextStripper对象来提取文本
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            // 提取文本内容
            String text = pdfTextStripper.getText(document);
            int references = text.lastIndexOf("References");
            if (references!=-1){
                text = text.substring(0, references);
            }
            // 打印提取的文本内容
            log.info("{}: {}", pdfFile.getName(), text.length());
            // 关闭文档
            document.close();
            return text;
        } catch (IOException e) {
            log.error("", e);
        }
        throw new RuntimeException("文件读取失败");
    }
}
