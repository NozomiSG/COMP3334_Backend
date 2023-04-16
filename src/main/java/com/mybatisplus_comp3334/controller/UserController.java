package com.mybatisplus_comp3334.controller;


import com.mybatisplus_comp3334.service.concept.MailService;
import com.mybatisplus_comp3334.service.concept.UserService;
import com.mybatisplus_comp3334.entity.User;
import com.mybatisplus_comp3334.util.AESUtils;
import com.mybatisplus_comp3334.util.RedisUtils;
import com.mybatisplus_comp3334.util.EncryptionUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Log
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user-login")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    MailService mailService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    EncryptionUtils encryptionUtils;

    @Autowired
    AESUtils aesUtils;

    @RequestMapping(value = "/")
    public String index(){
        return "index";
    }

    @PostMapping("/register")
    public Map<String, Object> register(@ModelAttribute User user, @RequestParam String verificationCode) throws NoSuchAlgorithmException {
        log.info("register request");
        Map<String, Object> map = new HashMap<>(4);
        if (user == null) {
            log.info("register reject: invalid request");
            map.put("resultCode", "0");
            map.put("resultMsg", "register reject: invalid request");
            map.put("data", "");
        } else {
            String storedCode = (String)redisUtils.getCache(user.getUserEmail());
            if (storedCode == null) {
                log.info("register reject: not find verification code");
                map.put("resultCode", "0");
                map.put("resultMsg", "register reject: not find verification code");
                map.put("data", "");
            }
            else if (!storedCode.equals(verificationCode)){
                log.info("register reject: unmatched verification code");
                map.put("resultCode", "0");
                map.put("resultMsg", "register reject: unmatched verification code");
                map.put("data", "");
            }
            else {
                log.info("register accept");
                log.info("Generate user public key and private key");
                String[] keyPair = encryptionUtils.generateKeyPair();
                user.setPrivateKey(keyPair[0]);
                user.setPublicKey(keyPair[1]);
                userService.insertUserInfo(user);
                map.put("resultCode", "1");
                map.put("resultMsg", "register accept");
                Map<String, String> tem = new HashMap<>(4);
                tem.put("id", user.getUserId().toString());
                tem.put("email", user.getUserEmail());
                map.put("data", tem);
            }
        }
        return map;
    }

    @GetMapping("/email-verify")
    @ResponseBody
    public Map<String, Object> emailVerify(@RequestParam String to) {
        Map<String, Object> map = new HashMap<>(3);
        log.info("email verify request");
        log.info("Check whether already register");
        User checkUser = userService.selectUserByEmail(to);
        if (checkUser != null) {
            log.info("email already registered");
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: email already registered");
            map.put("data", "reject");
            return map;
        }
        String uuid = generateCode();
        mailService.sentSimpleMail(to,
                "[P2P Mall verification code] Welcome!",
                "Your verification code is:\n" +
                        uuid + "\n" + "It will expire after 5 mins\n");
        redisUtils.setCacheWithExpire(to, uuid, 5);
        map.put("resultCode", "1");
        map.put("resultMsg", "verify accept: verification code sent");
        map.put("data", "ok");
        return map;
    }

    @GetMapping("/login")
    public Map<String, Object> login(@RequestParam String email, @RequestParam String pwd) {
        log.info("login request");
        User queryResult = userService.selectUserByEmail(email);
        Map<String, Object> map = new HashMap<>(3);
        map.put("resultCode", "0");
        // verify existence.
        if (queryResult == null) {
            log.info("login reject: no such user");
            map.put("resultMsg", "login reject: no such user");
            map.put("data", "reject");
            return map;
        }
        // verify password
        if (pwd.equals(queryResult.getUserPassword())) {
            log.info("login accept");
            map.replace("resultCode", "1");
            map.put("resultMsg", "login accept");
            Map<String, String> tem = new HashMap<>(4);
            tem.put("id", queryResult.getUserId().toString());
            tem.put("email", queryResult.getUserEmail());
            log.info(tem.toString());
            map.put("data", tem);
        }
        else {
            log.info("login reject: error password");
            map.put("resultMsg", "login reject: error password");
            map. put("data", "reject");
        }
        return map;
    }

    @GetMapping("/email-login")
    public Map<String, Object> emailLogin(@RequestParam String email) {
        Map<String, Object> map = new HashMap<>(3);
        log.info("email verify request");
        log.info("Check whether already register");
        User checkUser = userService.selectUserByEmail(email);
        if (checkUser == null) {
            log.info("email haven't registered");
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: email haven't registered");
            map.put("data", "reject");
        }
        else {
            log.info("Generate key for verifying user identity");
            String key = generateKey();
            redisUtils.setCacheWithExpire(email, key, 5);
            log.info("Send key to user, key:"+key);
            map.put("resultCode", "1");
            map.put("resultMsg", "verify accept: key sent");
            map.put("data", key);
        }
        return map;
    }

    @GetMapping("/password-verify")
    public Map<String, Object> passwordVerify(@RequestParam String email, @RequestParam String encryptedKey) throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Map<String, Object> map = new HashMap<>(3);
        log.info("Check whether already register");
        User checkUser = userService.selectUserByEmail(email);
        if (checkUser == null) {
            log.info("email haven't registered");
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: email haven't registered");
            map.put("data", "reject");
            return map;
        }
        log.info("Check whether key exists");
        String storedKey = (String)redisUtils.getCache(email);
        if (storedKey == null) {
            log.info("verify reject: not find key");
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: not find key");
            map.put("data", "reject");
            return map;
        }
        log.info("check whether key is matched");
        String encryptedPassword = aesUtils.Encrypt(checkUser.getUserPassword(), storedKey, "0000000000000000");
        if (!encryptedPassword.equals(encryptedKey)) {
            log.info("verify reject: unmatched key");
            log.info("encrypted of real password:"+encryptedPassword);
            log.info("encrypted from user:"+encryptedKey);
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: unmatched key");
            map.put("data", "reject");
        } else {
            log.info("login accept");
            map.put("resultCode", "1");
            map.put("resultMsg", "login accept");
            log.info("Generate dynamic RSA for transformation encryption");
            String[] keypair = encryptionUtils.generateKeyPair();
            log.info("Store dynamic RSA private key in redis");
            // Password will be stored in redis for 200mins
            redisUtils.setCacheWithExpire(checkUser.getUserId()+"_privateKey", keypair[1], 200);
            Map<String, String> tem = new HashMap<>(4);
            tem.put("id", checkUser.getUserId().toString());
            tem.put("email", checkUser.getUserEmail());
            tem.put("publicKey", keypair[0]);
            log.info(tem.toString());
            map.put("data", tem);
        }
        return map;
    }

    @GetMapping("/connect-establishment")
    public Map<String, Object> receiveRSAFromUser(@RequestParam Long id, @RequestParam String encryptedPublicKey) throws Exception {
        Map<String, Object> map = new HashMap<>(3);
        log.info("Get private key from redis");
        String privateKey = (String)redisUtils.getCache(id+"_privateKey");
        if (privateKey == null) {
            log.info("verify reject: not find key");
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: not find key");
            map.put("data", "reject");
            return map;
        }
        log.info("Decrypt public key from user");
        String publicKeyFromUser = encryptionUtils.decrypt(encryptedPublicKey, privateKey);
        log.info("Store public key in redis");
        redisUtils.setCacheWithExpire(id+"_publicKey", publicKeyFromUser, 200);
        return map;
    }


    private static String generateCode() {
        long codeL = System.nanoTime();
        String codeStr = Long.toString(codeL);
        return codeStr.substring(codeStr.length() - 6);
    }

    private static String generateKey() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(62);
            stringBuilder.append(str.charAt(number));
        }
        return stringBuilder.toString();

    }



}
