package com.king.model;

import com.king.util.ImageCreateUtil;
import lombok.Data;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 生成图片参数控制
 *
 * @author h
 * @date Created in 2020/5/30 17:25
 */
@Data
public class ImageCreateEntity {
    /**
     * 宽度
     */
    private Integer width = 800;
    /**
     * 高度
     */
    private Integer height = 800;
    /*==============图片内容==============*/
    /**
     * 图片内容
     */
    private BufferedImage imgContent;
    /**
     * 图片宽度
     */
    private int imgWidth = 100;
    /**
     * 图片宽度
     */
    private int imgHeight = 100;
    /**
     * 图片渲染X起点
     */
    private int imgX;
    /**
     * 图片渲染Y轴起点
     */
    private int imgY;
    /*=====================文本内容===================*/
    /**
     * 文本内容
     */
    private String textContent;
    /**
     * 字体名称
     */
    private String fontName = "";
    /**
     * 字体文件路径
     */
    private String fontFilePath = "/home/appuser/mirai_workplace/font/simsun.ttc";
    /**
     * 字体尺寸
     */
    private int fontSize = 30;
    /**
     * 字体风格
     */
    private int fontStyle = Font.PLAIN;
    /**
     * 字体颜色
     */
    private Color fontColor = Color.BLACK;
    /**
     * 字体间距，默认值为零，与当前字体尺寸相关
     */
    private Integer fontSpace = 20;
    /**
     * 行距
     */
    private Integer linePadding = 5;
    /**
     * 文本透明度：值从0-1.0，依次变得不透明
     */
    private float textTransparency = 1.0f;
    /**
     * 文本渲染X起点
     */
    private Integer textX = 5;
    /**
     * 文本渲染Y轴起点
     */
    private Integer textY = 5;
    /**
     * 左边距
     */
    private Integer textLeftPadding = 20;
    /**
     * 右边距
     */
    private Integer textRightPadding = 20;
    /**
     * 每行居中
     */
    private boolean isCenterLine=false;
    /*=================背景==============*/
    /**
     * 背景颜色
     */
    private Color backgroundColor = Color.WHITE;
    /**
     * 背景是否透明
     */
    private boolean isTransparentBackground = false;
    /**
     * 背景图片
     */
    private BufferedImage backgroundImg;

    private List<String> lines;

    public void setTextContent(String textContent) {
        this.textContent = textContent;
        generateLines();
    }

    public void generateLines(){
        int v = (int) (1.0 + fontSpace + fontSize);
        setTextLeftPadding(v);
        setTextRightPadding(v);
        setTextX(v);
        int contentWith = this.getWidth() - this.getTextLeftPadding() - this.getTextRightPadding();
        lines = ImageCreateUtil.getLines(textContent, contentWith, fontSpace, fontSize);
    }

}

