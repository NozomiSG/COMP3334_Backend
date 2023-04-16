package com.mybatisplus_comp3334.util;

import org.springframework.stereotype.Component;

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
import java.util.Base64;

@Component
public class AESUtils {

    public String Encrypt(String sSrc, String sKey, String sIv) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
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
        result = Base64.getEncoder().encodeToString(encrypted);//byte transform into hex output
        return result;
    }

}
