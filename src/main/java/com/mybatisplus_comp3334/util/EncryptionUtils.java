package com.mybatisplus_comp3334.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionUtils {

    public String[] getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024,new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // obtain private key
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // obtain public key
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // obtain private string
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // store public, private keys into keyMap
        return new String[]{publicKeyString, privateKeyString};
    }

    public String encrypt(String str, String publicKey) throws Exception{
        //base64 encoded public key
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA encryption
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String Str = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return Str;
    }

    public String decrypt(String str, String privateKey) throws Exception {
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64 encoded private key
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA decryption
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String Str = new String(cipher.doFinal(inputByte));
        return Str;
    }
}
