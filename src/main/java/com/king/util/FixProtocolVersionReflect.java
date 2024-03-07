package com.king.util;

import net.mamoe.mirai.utils.BotConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.EnumMap;

public class FixProtocolVersionReflect {
    public static EnumMap protocols;

    private FixProtocolVersionReflect(){}

    public static void main(String[] args) {
        System.out.println(toHexString("com.tencent.qq"));
    }

    public static String toHexString (String value){
        byte[] bytes = value.getBytes();
        StringBuffer data = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            data.append(Long.toHexString(bytes[i]));
            data.append(" ");
        }
        return data.toString().trim().toUpperCase();
    };
    /**
     * 升级协议
     */
    public static void update(){
        try {
            Class miraiProtocolInternalClass = Class.forName("net.mamoe.mirai.internal.utils.MiraiProtocolInternal");
            Constructor constructor = miraiProtocolInternalClass.getConstructor(
                    String.class,
                    long.class,
                    String.class,
                    String.class,
                    int.class,
                    int.class,
                    int.class,
                    String.class,
                    long.class,
                    int.class);

            Field protocolsField = miraiProtocolInternalClass.getDeclaredField("protocols");
            protocolsField.setAccessible(true);
            protocols = (EnumMap) protocolsField.get(EnumMap.class);


            protocols.put(BotConfiguration.MiraiProtocol.ANDROID_PHONE, constructor.newInstance(
                    "com.tencent.mobileqq",
                    537151682,
                    "8.9.33.10335",
                    "6.0.0.2534",
                    150470524,
                    0x10400,
                    16724722,
                    "A6 B7 45 BF 24 A2 C2 77 52 77 16 F6 F3 6E B6 8D",
                    1673599898L,
                    19
            ));



            protocols.put(BotConfiguration.MiraiProtocol.ANDROID_PAD, constructor.newInstance(
                    "com.tencent.mobileqq",
                    537151218,
                    "8.9.33.10335",
                    "6.0.0.2534",
                    150470524,
                    0x10400,
                    16724722,
                    "A6 B7 45 BF 24 A2 C2 77 52 77 16 F6 F3 6E B6 8D",
                    1673599898L,
                    19
            ));

            protocols.put(BotConfiguration.MiraiProtocol.IPAD, constructor.newInstance(
                    "com.tencent.minihd.qq",
                    537151363,
                    "8.9.33.614",
                    "6.0.0.2433",
                    150470524,
                    66560,
                    1970400,
                    "AA 39 78 F4 1F D9 6F F9 91 4A 66 9E 18 64 74 C7",
                    1640921786L,
                    12
            ));



            //无法使用
//            protocols.put(BotConfiguration.MiraiProtocol.MACOS, constructor.newInstance(
//                    "com.tencent.qq",
//                    0x2003ca32,
//                    "6.7.9",
//                    "6.2.0.1023",
//                    0x7ffc,
//                    66560,
//                    1970400,
//                    toHexString("com.tencent.qq"),
//                    0L,
//                    7
//            ));

            //还原成低版本
            protocols.put(BotConfiguration.MiraiProtocol.MACOS, constructor.newInstance(
                    "com.tencent.minihd.qq",
                    537128930,
                    "5.8.9",
                    "6.0.0.2433",
                    150470524,
                    66560,
                    1970400,
                    "AA 39 78 F4 1F D9 6F F9 91 4A 66 9E 18 64 74 C7",
                    1595836208L,
                    12
            ));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
