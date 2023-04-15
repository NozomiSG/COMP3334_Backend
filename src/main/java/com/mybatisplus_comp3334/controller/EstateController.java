package com.mybatisplus_comp3334.controller;


import com.mybatisplus_comp3334.entity.Estate;
import lombok.Generated;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mybatisplus_comp3334.service.concept.EstateService;

import java.util.HashMap;
import java.util.Map;

@Log
@RestController
@RequestMapping("/transaction")
public class EstateController {

    @Autowired
    private EstateService estateService;


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


}
