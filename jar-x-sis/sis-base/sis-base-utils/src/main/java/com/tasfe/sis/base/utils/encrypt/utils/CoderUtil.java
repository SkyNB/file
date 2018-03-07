package com.tasfe.sis.base.utils.encrypt.utils;


import com.tasfe.sis.base.utils.encrypt.Encoder;

public class CoderUtil {
    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";

    /**
     * BASE64解密
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) {
        return Encoder.base64ToByteArray(key);
    }

    /**
     * BASE64加密
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return Encoder.byteArrayToBase64(key);
    }
}
