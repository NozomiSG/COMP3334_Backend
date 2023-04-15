package com.mybatisplus_comp3334.controller;


import com.mybatisplus_comp3334.service.concept.MailService;
import com.mybatisplus_comp3334.service.concept.UserService;
import com.mybatisplus_comp3334.entity.User;
import com.mybatisplus_comp3334.util.RedisUtils;
import com.mybatisplus_comp3334.util.EncryptionUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Log
@RestController
@RequestMapping("/user-login")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    MailService mailService;

    @Autowired
    RedisUtils redisUtils;

    EncryptionUtils encryptionUtils;

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
                tem.put("name", user.getUserName());
                tem.put("id", user.getUserId().toString());
                tem.put("email", user.getUserEmail());
                map.put("data", tem);
            }
        }
        return map;
    }

    @GetMapping("/email-verify")
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
            Map<String, String> tem = new HashMap<String, String>(4);
            tem.put("name", queryResult.getUserName());
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

    private static String generateCode() {
        long codeL = System.nanoTime();
        String codeStr = Long.toString(codeL);
        return codeStr.substring(codeStr.length() - 6);
    }





}
