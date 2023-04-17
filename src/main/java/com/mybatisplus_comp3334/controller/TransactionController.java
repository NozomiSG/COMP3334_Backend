package com.mybatisplus_comp3334.controller;

import com.mybatisplus_comp3334.entity.Estate;
import com.mybatisplus_comp3334.entity.TransactionRecord;
import com.mybatisplus_comp3334.entity.User;
import com.mybatisplus_comp3334.service.concept.EstateService;
import com.mybatisplus_comp3334.service.concept.TransactionService;
import com.mybatisplus_comp3334.service.concept.UserService;
import com.mybatisplus_comp3334.util.EncryptionUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private UserService userService;

    @Autowired
    EncryptionUtils encryptionUtils;

    @PostMapping("/insert-transaction") //to be edited after completion of front end: input as encrypted/signatured String
    public Map<String, Object> insertTransaction(@RequestParam Long buyerId, @RequestParam Long sellerId, @RequestParam String transString) throws Exception {
        log.info("insert transaction request");
        Map<String, Object> map = new HashMap<>(4);

        User buyer = userService.selectUserInfoById(buyerId);
        User seller = userService.selectUserInfoById(sellerId);

        if (buyer == null) {
            log.info("buyer does not exist");
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: buyer does not exist");
            map.put("data", "reject");
            return map;
        } //judge whether buyer exists

        if (seller == null) {
            log.info("seller does not exist");
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: seller does not exist");
            map.put("data", "reject");
            return map;
        } //judge whether seller exists

        String bKey = buyer.getPrivateKey(), sKey = seller.getPrivateKey();

        String trans_buyerDecrypted = encryptionUtils.decrypt(transString, bKey);
        String transString_decrypted = encryptionUtils.decrypt(trans_buyerDecrypted, sKey);

        try { //sample json format: "{}"
            new ObjectMapper().readTree(transString_decrypted);
            Timestamp currentTime = new Timestamp(date.getTime());

            Estate estate = new Estate();
            //transactionService.insertTransactionInfo(estate);
            //to be continued...

        } catch (Exception e) { //string not able to recognise
            log.info("insert transaction reject: invalid request");
            map.put("resultCode", "0");
            map.put("resultMsg", "insert transaction reject: invalid request");
            map.put("data", "");
        }
        /*
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

        }*/
        return map;
    }


}
