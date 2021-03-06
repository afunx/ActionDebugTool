package com.afunx.client.util;

/**
 * Created by afunx on 22/12/2017.
 */

public class HexUtils {

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * byte转hexString
     *
     * @param b byte
     * @return 16进制大写字符串
     */
    public static String byte2HexString(byte b) {
        StringBuilder sb = new StringBuilder();
        sb.append(hexDigits[b >>> 4 & 0x0f]);
        sb.append(hexDigits[b & 0x0f]);
        return sb.toString();
    }

    /**
     * byteArr转hexString
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes 字节数组
     * @return 16进制大写字符串
     */
    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (bytes.length == 0) {
            return "";
        }
        return bytes2HexString(bytes, 0, bytes.length);
    }

    /**
     * byteArr转hexString
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes  字节数组
     * @param offset 开始位置
     * @param count  字节长度
     * @return 16进制大写字符串
     */
    public static String bytes2HexString(byte[] bytes, int offset, int count) {
        if (bytes == null || offset < 0 || count <= 0 || count - offset > bytes.length) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(hexDigits[bytes[i + offset] >>> 4 & 0x0f]);
            sb.append(hexDigits[bytes[i + offset] & 0x0f]);
            if (i != count - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

}

