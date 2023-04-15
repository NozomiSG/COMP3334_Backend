package com.mybatisplus_comp3334.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AES {

    private static String Encrypt(String sSrc, String sKey, String sIv) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // TODO Auto-generated method stub

        String result;//

        if (sKey == null) {
            System.out.print("null key");
            return null;
        }
        // judge whether the Key is hex
        if (sKey.length() != 16) {
            System.out.print("Key length less than 16");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(sIv.getBytes());//apply CBC format, apply a vector iv, which can enhance the strength of the encryption
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        //return new Base64().encode(encrypted);

        result = byteToHex(encrypted);//byte transform into hex output

        return result;

    }

    private static String byteToHex(byte[] bytes) {
        // TODO Auto-generated method stub

        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
