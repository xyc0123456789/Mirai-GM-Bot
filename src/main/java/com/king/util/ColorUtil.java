package com.king.util;

import java.awt.*;
import java.util.Random;

public class ColorUtil {

    public static Color[] generateRandomColor(int n){
        Random random = new Random();
        // 创建一个长度为15的Color数组
        Color[] colors = new Color[n];
        // 循环15次，生成随机的RGB颜色值，并存入数组
        for (int i = 0; i < n; i++) {
            // 随机生成0到255之间的整数，作为红色分量
            int r = random.nextInt(256);
            // 随机生成0到255之间的整数，作为绿色分量
            int g = random.nextInt(256);
            // 随机生成0到255之间的整数，作为蓝色分量
            int b = random.nextInt(256);
            // 根据RGB颜色值创建一个Color对象，并存入数组
            colors[i] = new Color(r, g, b);
        }
        return colors;
    }

    public static Color[] generateRandomColorBlack(int n){
        Random random = new Random();
        // 创建一个长度为15的Color数组
        Color[] colors = new Color[n];
        // 循环15次，生成随机的RGB颜色值，并存入数组
        for (int i = 0; i < n; i++) {
            int top = 330;
            // 随机生成0到255之间的整数，作为红色分量
            int r = random.nextInt(256);
            top = top-r;
            // 随机生成0到255之间的整数，作为绿色分量
            int g = random.nextInt(Math.min(256, top));
            top = top-g;
            // 随机生成0到255之间的整数，作为蓝色分量
            int b = random.nextInt(Math.min(256, top));
            // 根据RGB颜色值创建一个Color对象，并存入数组
            colors[i] = new Color(r, g, b);
        }
        return colors;
    }

    public static final Color[] COLORS = {Color.BLACK, Color.BLUE, Color.ORANGE, Color.RED, Color.DARK_GRAY, Color.PINK, Color.MAGENTA};

    public static Color[] generateWithBase(int n){
        Color[] ans = new Color[n+COLORS.length];
        Color[] randomColor = generateRandomColorBlack(n);
        for (int i=0;i<ans.length;i++){
            if (i < randomColor.length){
                ans[i] = randomColor[i];
            }else {
                ans[i] = COLORS[i- randomColor.length];
            }
        }
        return ans;
    }

}
