package com.mybatisplus_comp3334.controller;


import com.mybatisplus_comp3334.entity.Estate;
import com.mybatisplus_comp3334.service.concept.UserService;
import com.mybatisplus_comp3334.util.EncryptionUtils;
import com.mybatisplus_comp3334.util.RedisUtils;
import lombok.Generated;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mybatisplus_comp3334.service.concept.EstateService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/estate")
public class EstateController {

    @Autowired
    private EstateService estateService;

    @Autowired
    EncryptionUtils encryptionUtils;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    UserService userService;


    @RequestMapping(value = "/")
    public String index(){
        return "index";
    }

    @PostMapping("/insert-estate")
    public Map<String, Object> insertEstate(@ModelAttribute Estate estate) {
        log.info("insert estate request");
        Map<String, Object> map = new HashMap<>(4);
        if (estate == null) {
            log.info("insert estate reject: invalid request");
            map.put("resultCode", "0");
            map.put("resultMsg", "insert estate reject: invalid request");
            map.put("data", "");
        } else {
            log.info("insert estate accept");
            estateService.insertEstateInfo(estate);
            map.put("resultCode", "1");
            map.put("resultMsg", "insert estate accept");
            map.put("data", "");
        }
        return map;
    }

    @PostMapping("/update-estate")
    public Map<String, Object> updateEstate(@ModelAttribute Estate estate) {
        log.info("update estate request");
        Map<String, Object> map = new HashMap<>(4);
        if (estate == null) {
            log.info("update estate reject: invalid request");
            map.put("resultCode", "0");
            map.put("resultMsg", "update estate reject: invalid request");
            map.put("data", "");
        } else {
            log.info("update estate accept");
            estateService.updateEstateInfo(estate);
            map.put("resultCode", "1");
            map.put("resultMsg", "update estate accept");
            map.put("data", "");
        }
        return map;
    }

    @GetMapping("/update-owner")
    public Map<String, Object> updateOwnerById(Long estateId, Long ownerId) {
        log.info("update owner request");
        Map<String, Object> map = new HashMap<>(4);
        if (estateId == null || ownerId == null) {
            log.info("update owner reject: invalid request");
            map.put("resultCode", "0");
            map.put("resultMsg", "update owner reject: invalid request");
            map.put("data", "");
        } else {
            Estate estate = estateService.selectEstateInfoById(estateId);
            if (estate == null) {
                log.info("update owner reject: estate not exist");
                map.put("resultCode", "0");
                map.put("resultMsg", "update owner reject: estate not exist");
                map.put("data", "");
                return map;
            } else {
                log.info("update owner accept");
                estate.setEstateOwnerId(ownerId);
                estateService.updateEstateInfo(estate);
                map.put("resultCode", "1");
                map.put("resultMsg", "update owner accept");
                map.put("data", "");
            }
        }
        return map;
    }

    @GetMapping("/request-self-estate-info")
    public Map<String, Object> requestEstateInfoFromUser(@RequestParam Long id) throws Exception {
        Map<String, Object> map = new HashMap<>(3);

        log.info("get private key by id");
        String privateKey = (String)redisUtils.getCache(id+"_privateKey");
        String publicKey = (String)redisUtils.getCache(id+"_publicKey");
        if (privateKey == null||publicKey == null) {
            log.info("verify reject: not find key");
            map.put("resultCode", "-1");
            map.put("resultMsg", "verify reject: not find key");
            map.put("data", "reject");
            return map;
        }

        log.info("request estate info from user request");
        if (estateService.selectEstateInfoByOwnerId(id) == null) {
            log.info("request estate info from user reject: invalid request");
            map.put("resultCode", "0");
            map.put("resultMsg", "request estate info from user reject: invalid request");
            map.put("data", "reject");
        } else {
//            String encryptedEstateInfo = encryptionUtils.encrypt(estateService.selectEstateInfoByOwnerId(id).toString(), publicKey);
            log.info("request estate info from user accept");
            map.put("resultCode", "1");
            map.put("resultMsg", "request estate info from user accept");
            map.put("data", estateService.selectEstateInfoByOwnerId(id));
        }
        return map;
    }

    @GetMapping("/request-all-estate-info")
    public Map<String, Object> requestEstateAllInfo(@RequestParam Long id) throws Exception {
        Map<String, Object> map = new HashMap<>(3);

        log.info("get private and public key by id");
        String privateKey = (String)redisUtils.getCache(id+"_privateKey");
        String publicKey = (String)redisUtils.getCache(id+"_publicKey");
        if (privateKey == null||publicKey == null) {
            log.info("verify reject: not find key");
            map.put("resultCode", "-1");
            map.put("resultMsg", "verify reject: not find key");
            map.put("data", "reject");
            return map;
        }
        log.info("request estate all info request");
        if (estateService.selectAllEstateInfo() == null) {
            log.info("request estate all info reject: invalid request");
            map.put("resultCode", "0");
            map.put("resultMsg", "request estate all info reject: invalid request");
            map.put("data", "reject");
        } else {
            List<Estate> estates = estateService.selectAllEstateInfo();
            log.info("Encrypt estate: "+estates.toString());
//            String encryptedEstateInfo = encryptionUtils.encrypt(estateService.selectAllEstateInfo().toString(), publicKey);
            log.info("request estate all info accept");
            map.put("resultCode", "1");
            map.put("resultMsg", "request estate all info accept");
            map.put("data", estateService.selectAllEstateInfo());
        }
        return map;
    }

    public Map<String, Object> toMapResult(String msg, List<Estate> lst, String publicKey) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("resultCode", "1");
        map.put("resultMsg", msg);
        List<String> tmp = new ArrayList<>();
        for(Estate x: lst) {
            tmp.add(encryptionUtils.encrypt(x.toString(), publicKey));
        }
        map.put("data", tmp);
        return map;
    }
}
