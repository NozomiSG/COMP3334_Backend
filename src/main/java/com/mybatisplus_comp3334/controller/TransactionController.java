package com.mybatisplus_comp3334.controller;

import com.mybatisplus_comp3334.entity.Estate;
import com.mybatisplus_comp3334.entity.Transaction;
import com.mybatisplus_comp3334.entity.User;
import com.mybatisplus_comp3334.service.concept.EstateService;
import com.mybatisplus_comp3334.service.concept.TransactionService;
import com.mybatisplus_comp3334.service.concept.UserService;
import com.mybatisplus_comp3334.util.EncryptionUtils;
import com.mybatisplus_comp3334.util.HashUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
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

    @Autowired
    HashUtils hashUtils;

    @PostMapping("/insert-transaction") //to be edited after completion of front end: input as encrypted/signature String
    public Map<String, Object> insertTransaction(@RequestParam Long buyerId, @RequestParam Long sellerId, @RequestParam String transString1, @RequestParam String transString2) throws Exception {
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


        try { //sample json format: "{}"
            log.info("decrypt transaction info");
            String bKey = buyer.getPrivateKey(), sKey = seller.getPrivateKey();
            String trans_buyerDecrypted = encryptionUtils.decrypt(transString1, sKey)+encryptionUtils.decrypt(transString2, sKey);
            String transString_decrypted = encryptionUtils.decrypt(trans_buyerDecrypted, bKey);
            log.info("transform decrypted string into json");
            log.info("transString_decrypted: "+transString_decrypted);
            JsonNode rootNode = new ObjectMapper().readTree(transString_decrypted); //read json

            Date date = new Date();
            Long estateId = Long.valueOf(rootNode.get("estateId").asText());
            Timestamp currentTime = new Timestamp(date.getTime());
            Transaction newRec = new Transaction();

            newRec.setBuyerId(buyerId);
            newRec.setSellerId(sellerId);
            newRec.setEstateId(estateId);
            newRec.setTransTime(currentTime);
            newRec.setTransStatus(true);
            newRec.setTransPrice(Integer.valueOf(rootNode.get("estatePrice").asText()));
            newRec.setSignature(transString1+transString2);

            transactionService.updateTransactionInfo(newRec);
            Estate currentEstate = estateService.selectEstateInfoById(estateId);
           currentEstate.setEstateOwnerId(buyerId);
           currentEstate.setEstatePrice(Integer.valueOf(rootNode.get("estatePrice").asText()));
           currentEstate.setEstateStatus(false);
           estateService.updateEstateInfo(currentEstate);
            //transactionService.insertTransactionInfo(estate);
            //to be continued...
            map.put("resultCode", "1");
            map.put("resultMsg", "insert transaction accept");
            map.put("data", "accept");

        } catch (Exception e) { //string not able to recognise
            log.info("Output exception: " + e.getMessage());
            log.info("insert transaction reject: invalid request");
            map.put("resultCode", "0");
            map.put("resultMsg", "insert transaction reject: invalid request");
            map.put("data", "reject");
        }
        return map;
    }

    @GetMapping("/request-transaction")
    public Map<String, Object> requestTransaction(@RequestParam Long userId, @RequestParam String buyer_signature) throws Exception {
        log.info("request transaction request");
        Map<String, Object> map = new HashMap<>(4);
        User buyer = userService.selectUserInfoById(userId);

        if (buyer == null) {
            log.info("buyer does not exist");
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: buyer does not exist");
            map.put("data", "reject");
            return map;
        } //judge whether buyer exists

        String bKey = buyer.getPrivateKey();
        log.info("Decrypted buyer signature");
        String decrypted = encryptionUtils.decrypt(buyer_signature, bKey);
        try {
            JsonNode rootNode = new ObjectMapper().readTree(decrypted); //read json
            log.info("Decrypted buyer signature: " + rootNode);
            Long sellerId = Long.valueOf(rootNode.get("sellerId").asText());
            Long estateId = Long.valueOf(rootNode.get("estateId").asText());
            Integer estatePrice = Integer.valueOf(rootNode.get("estatePrice").asText());
            Transaction newRec = new Transaction();
            log.info("check whether exist undone transaction");
            Transaction currentTrans = transactionService.selectUndoneTransactionByEstateId(estateId);

            if (currentTrans != null) {
                log.info("compare price");
                if (currentTrans.getTransPrice() > estatePrice) {
                    log.info("request transaction reject: price lower than current transaction");
                    map.put("resultCode", "0");
                    map.put("resultMsg", "request transaction reject: price lower than current transaction");
                    map.put("data", "reject");
                    return map;
                } else {
                    log.info("delete current transaction");
                    transactionService.deleteUndoneTransactionByEstateId(estateId);
                }
            }
            log.info("set transaction info");
            newRec.setBuyerId(userId);
            newRec.setSellerId(sellerId);
            newRec.setEstateId(estateId);
            newRec.setTransPrice(estatePrice);
            newRec.setSignature(buyer_signature);
            transactionService.insertTransactionInfo(newRec);
            log.info("change estate status");
            Estate currentEstate = estateService.selectEstateInfoById(estateId);
            currentEstate.setEstateStatus(true);
            currentEstate.setEstatePrice(estatePrice);
            estateService.updateEstateInfo(currentEstate);
            log.info("successfully change estate status");
            log.info("successfully insert transaction info");
            map.put("resultCode", "1");
            map.put("resultMsg", "request transaction sent");
            map.put("data", "accept");

        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
            log.info("request transaction reject: invalid request");
            map.put("resultCode", "0");
            map.put("resultMsg", "request transaction reject: invalid request");
            map.put("data", "reject");
        }
        return map;
    }

    @GetMapping("/accept-transaction")
    public Map<String, Object> acceptTransaction(@RequestParam Long userId, @RequestParam Long estateId) throws Exception { //userId - seller
        log.info("accept transaction request");
        Map<String, Object> map = new HashMap<>(4);

        User seller = userService.selectUserInfoById(userId);

        if (seller == null) {
            log.info("seller does not exist");
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: seller does not exist");
            map.put("data", "reject");
            return map;
        }
        Transaction transaction = transactionService.selectUndoneTransactionByEstateId(estateId);
        if (transaction == null) {
            log.info("transaction does not exist");
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: transaction does not exist");
            map.put("data", "reject");
            return map;
        }
        if (transaction.getSellerId() != userId) {
            log.info("seller does not match");
            map.put("resultCode", "0");
            map.put("resultMsg", "verify reject: seller does not match");
            map.put("data", "reject");
            return map;
        }
        log.info("return transaction info");
        map.put("resultCode", "1");
        map.put("resultMsg", "successfully accepted transaction");
        log.info("transaction info: " + transaction);
        map.put("data", transaction);
        return map;
    }
}
