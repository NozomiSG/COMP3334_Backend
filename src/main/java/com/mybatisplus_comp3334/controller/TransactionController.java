package com.mybatisplus_comp3334.controller;

import com.mybatisplus_comp3334.entity.Estate;
import com.mybatisplus_comp3334.entity.Transaction;
import com.mybatisplus_comp3334.entity.User;
import com.mybatisplus_comp3334.service.concept.EstateService;
import com.mybatisplus_comp3334.service.concept.TransactionService;
import com.mybatisplus_comp3334.service.concept.UserService;
import com.mybatisplus_comp3334.util.EncryptionUtils;
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

    @PostMapping("/insert-transaction") //to be edited after completion of front end: input as encrypted/signature String
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
            JsonNode rootNode = new ObjectMapper().readTree(transString_decrypted); //read json

            Date date = new Date();
            Long estateId = Long.valueOf(rootNode.get("estateId").asText());
            Timestamp currentTime = new Timestamp(date.getTime());

            Transaction newRec = new Transaction();

            newRec.setBuyerId(buyerId);
            newRec.setSellerId(sellerId);
            newRec.setEstateId(estateId);
            newRec.setTransTime(currentTime);

            transactionService.insertTransactionInfo(newRec);

            estateService.selectEstateInfoById(estateId).setEstateOwnerId(buyerId);
            //transactionService.insertTransactionInfo(estate);
            //to be continued...
            map.put("resultCode", "1");
            map.put("resultMsg", "insert transaction accept");
            map.put("data", "");

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
    public Map<String, Object> acceptTransaction(@RequestParam Long userId, @RequestParam Long transactionId) throws Exception { //userId - seller
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

        String sKey = seller.getPrivateKey();

        Transaction transaction = transactionService.selectTransactionInfoByTransId(transactionId);
        Estate estate = estateService.selectEstateInfoById(transaction.getEstateId());

        String newSignature = encryptionUtils.encrypt(transaction.getSignature(), seller.getPrivateKey());
        transaction.setSignature(newSignature);

        estate.setEstateOwnerId(transaction.getBuyerId());
        transaction.setTransStatus(false);

        estate.setEstateStatus(false);

        estateService.updateEstateInfo(estate);
        transactionService.updateTransactionInfo(transaction);

                log.info("successfully change estate status to false");
        return map;
    }
}
