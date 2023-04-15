package com.mybatisplus_comp3334.controller;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class messageController {
	private static Map<Integer, String> keyMap = new HashMap<Integer, String>();
	//defined pack for message transport, which requires userID and respective text message sent
	public class msgpack{
		 private Long userId;
		 private String textMsg;

		 public void __init__(Long ID, String str) {
			this.userID = ID;
			this.textMsg = str;
		 }
	}

	public static void getKeyPair() throws NoSuchAlgorithmException {  
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");   
		keyPairGen.initialize(1024,new SecureRandom());  
		KeyPair keyPair = keyPairGen.generateKeyPair();  
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // obtain private key
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // obtain public key
		String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));  
		// obtain private string
		String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));  
		// store public, private keys into keyMap
		keyMap.put(0,publicKeyString);  //0 for public
		keyMap.put(1,privateKeyString);  //1 for private
	}  
	
	public static String encrypt(String str, String publicKey) throws Exception{
		//base64 encoded public key
		byte[] decoded = Base64.decodeBase64(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
		//RSA encryption
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String Str = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
		return Str;
	}
 
	public static String decrypt(String str, String privateKey) throws Exception{
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

	public msgpack encryptPack(msgpack pack){
		msgpack retPack = new msgpack().__init__(pack.userId, encrypt(pack.textMsg, keyMap.get(0)));
		return retPack;
	}

	public msgpack decryptPack(msgpack pack){
		msgpack retPack = new msgpack().__init__(pack.userId, decrypt(pack.textMsg, keyMap.get(0)));
		return retPack;
	}

	//test
	public static void main(String[] args) throws Exception {
		getKeyPair();

		String message = "COMP3334";
		System.out.println("Random Public Key:" + keyMap.get(0));
		System.out.println("Random Private Key:" + keyMap.get(1));
		String messageEn = encrypt(message,keyMap.get(0));
		System.out.println(message + "\tENCRYPTED MESSAGE:" + messageEn);
		String messageDe = decrypt(messageEn,keyMap.get(1));
		System.out.println("DECRYPTED MESSAGGE:" + messageDe);
	}
}
