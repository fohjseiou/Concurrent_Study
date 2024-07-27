package com.ykdz.utils;

import java.nio.charset.StandardCharsets;

public class utf8 {
        public static void main(String[] args) {

            // 输入的十六进制字符串
            String hexString = " FB FB AF 00 00 00 00 00 04 23 12 14 07 13 01 03 23 10 07 12 00 10 18 03 1D 0D 1C 01 00 00 00 00 1F C5 70 43 52 B8 6D 43 B8 1E 6C 43 C3 55 CA 43 14 CE D6 43 00 80 DA 43 00 00 00 00 00 00 00 00 8F C2 F5 3E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 85 EB 47 42 00 50 AA 48 00 00 00 00 00 00 00 00 00 84 4B 49 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 D0 88 D1 41 10 11 D2 41 30 B6 D1 41 B0 D7 D3 41 00 00 F0 40 00 00 C0 40 00 00 00 00 00 00 00 00 E2 93 FD FD";
            String str = hexString.replace(" ", "");
            System.out.println(str);
            // 将十六进制字符串转换为字节数组
            byte[] byteArray = hexStringToByteArray(hexString);

            // 将字节数组解码为 UTF-8 字符串
            String utf8String = new String(byteArray, StandardCharsets.UTF_8);
            System.out.println("UTF-8 字符串：" + utf8String);
        }

    // 将十六进制字符串转换为字节数组
    public static byte[] hexStringToByteArray(String str) {
        int length = str.length();
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Hex string length must be even.");
        }
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4)
                    + Character.digit(str.charAt(i + 1), 16));
        }
        return byteArray;
    }
}
