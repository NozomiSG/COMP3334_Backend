package com.mybatisplus_comp3334.controller;

import com.mybatisplus_comp3334.entity.Estate;
import com.mybatisplus_comp3334.entity.TransactionRecord;
import com.mybatisplus_comp3334.entity.User;
import com.mybatisplus_comp3334.service.concept.EstateService;
import com.mybatisplus_comp3334.service.concept.TransactionService;
import com.mybatisplus_comp3334.util.EncryptionUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Log
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private EstateService estateService;

    @Autowired
    EncryptionUtils encryptionUtils;

    @PostMapping("/insert-transaction") //to be edited after completion of front end: input as encrypted/signatured String
    public Map<String, Object> insertTransaction(@RequestParam Long buyerId, @RequestParam Long sellerId, @RequestParam String transString) {
        log.info("insert transaction request");
        Map<String, Object> map = new HashMap<>(4);
        TransactionRecord checkUser = transactionService.
        if (checkUser == null) {
            log.info("email haven't registered");
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: email haven't registered");
            map.put("data", "reject");
            return map;
        }
        if (transaction == null) {
            log.info("insert transaction reject: invalid request");
            map.put("resultCode", "0");
            map.put("resultMsg", "insert transaction reject: invalid request");
            map.put("data", "");
        } else {
            log.info("insert transaction accept");
            transactionService.insertTransactionInfo(transaction);

            estateService.selectEstateInfoById(transaction.getEstateId()).setEstateOwnerId(transaction.getBuyerId());
            //Switch owner for the estate


            map.put("resultCode", "1");
            map.put("resultMsg", "insert transaction accept");
            map.put("data", "");

        }
        return map;
    }


}
