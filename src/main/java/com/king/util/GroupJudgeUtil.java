package com.king.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @description: 下载群头像，hash判断
 * @author: xyc0123456789
 * @create: 2023/6/17 13:38
 **/
@Slf4j
public class GroupJudgeUtil {

    public static final String DEFAULT0="0d2c29f3cbcb448c827192a635b7d2a2";
    public static final String DEFAULT40="185ff6f0cfc14f3bb8b838288d7dcc3c";
    public static final String DEFAULT41="185ff6f0cfc14f3bb8b838288d7dcc3c";
    public static final String DEFAULT100="fc691036c2bf602ac2ceebceb4d51b73";
    public static final String DEFAULT140="1be269a203cf2c13395f90b5fd919215";
    public static final String DEFAULT640="0d2c29f3cbcb448c827192a635b7d2a2";

    public static final String[] DEFAULT_LIST = new String[]{DEFAULT0, DEFAULT40, DEFAULT41, DEFAULT100, DEFAULT140, DEFAULT640};

    /**
     * 可以被设置默认头像绕过
     * @param groupId
     * @return
     */
    public static boolean judgeGroupIdExist(long groupId){
        String url = "https://p.qlogo.cn/gh/"+groupId+"/"+groupId+"/40";
        File image = FileUtil.downLoadImage(url, "download/image/group/" + groupId + ".png");
        String md5 = MD5Util.md5File(image.getAbsolutePath());
        FileUtil.deleteFile(image);
        boolean result = true;
        for (String hash: DEFAULT_LIST){
            if (hash.equals(md5)){
                result = false;
                break;
            }
        }
        log.info("url:{}, path:{}, md5:{} result:{}", url, image.getAbsolutePath(), md5, result);
        return result;
    }

    public static void main(String[] args) {
        judgeGroupIdExist(536348689);
    }

}
