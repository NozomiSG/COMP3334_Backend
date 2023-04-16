package com.mybatisplus_comp3334.controller;

import com.mybatisplus_comp3334.entity.Estate;
import com.mybatisplus_comp3334.entity.TransactionRecord;
import com.mybatisplus_comp3334.service.concept.EstateService;
import com.mybatisplus_comp3334.service.concept.TransactionService;
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
    private Estate estate;

    @PostMapping("/insert-transaction")
    public Map<String, Object> insertTransaction(@ModelAttribute TransactionRecord transaction) {
        log.info("insert transaction request");
        Map<String, Object> map = new HashMap<>(4);
        if (transaction == null) {
            log.info("insert transaction reject: invalid request");
            map.put("resultCode", "0");
            map.put("resultMsg", "insert transaction reject: invalid request");
            map.put("data", "");
        } else {
            log.info("insert transaction accept");
            transactionService.insertTransactionInfo(transaction);

            estate = estateService.selectEstateInfoById(transaction.getEstateId());
            estate.setEstateOwnerId(transaction.getBuyerId());
            estateService.updateEstateInfo(estate);

            map.put("resultCode", "1");
            map.put("resultMsg", "insert transaction accept");
            map.put("data", "");

        }
        return map;
    }


}
