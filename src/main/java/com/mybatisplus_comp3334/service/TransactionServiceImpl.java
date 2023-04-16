package com.mybatisplus_comp3334.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus_comp3334.entity.TransactionRecord;
import com.mybatisplus_comp3334.mapper.TransactionMapper;
import com.mybatisplus_comp3334.service.concept.TransactionService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public String insertTransactionInfo(TransactionRecord TransRecord) {
        log.info("Insert transaction info");
        if (transactionMapper.insert(TransRecord)>=0) {
            log.info("Success to insert transaction info");
            return "Success";
        } else {
            log.info("Fail to insert transaction info");
            return "Fail";
        }
    }

    @Override
    public TransactionRecord selectTransactionInfoByTransId(Long transId) {
        log.info("Select transaction info by transId");
        if (transactionMapper.selectById(transId) != null) {
            log.info("Success to select transaction info by transId");
            return transactionMapper.selectById(transId);
        } else {
            log.info("Fail to select transaction info by transId");
            return null;
        }
    }

    @Override
    public List<TransactionRecord> selectTransactionInfoByBuyerId(Long BuyerId) {
        log.info("Select transaction info by buyer id");
        transactionMapper.selectList(new QueryWrapper<>(new TransactionRecord()).eq("trans_buyer_id", BuyerId));
        if (transactionMapper.selectList(new QueryWrapper<>(new TransactionRecord()).eq("trans_buyer_id", BuyerId)) != null) {
            log.info("Success to select transaction info by buyer id");
            return transactionMapper.selectList(new QueryWrapper<>(new TransactionRecord()).eq("trans_buyer_id", BuyerId));
        } else {
            log.info("Fail to select transaction info by buyer id");
            return null;
        }
    }

    @Override
    public List<TransactionRecord> selectTransactionInfoBySellerId(Long SellerId) {
        log.info("Select transaction info by seller id");
        transactionMapper.selectList(new QueryWrapper<>(new TransactionRecord()).eq("trans_seller_id", SellerId));
        if (transactionMapper.selectList(new QueryWrapper<>(new TransactionRecord()).eq("trans_seller_id", SellerId)) != null) {
            log.info("Success to select transaction info by seller id");
            return transactionMapper.selectList(new QueryWrapper<>(new TransactionRecord()).eq("trans_seller_id", SellerId));
        } else {
            log.info("Fail to select transaction info by seller id");
            return null;
        }
    }
    @Override
    public List<TransactionRecord> selectTransactionInfoByEstateId(Long EstateId) {
        log.info("Select transaction info by estate id");
        transactionMapper.selectList(new QueryWrapper<>(new TransactionRecord()).eq("trans_estate_id", EstateId));
        if (transactionMapper.selectList(new QueryWrapper<>(new TransactionRecord()).eq("trans_estate_id", EstateId)) != null) {
            log.info("Success to select transaction info by estate id");
            return transactionMapper.selectList(new QueryWrapper<>(new TransactionRecord()).eq("trans_estate_id", EstateId));
        } else {
            log.info("Fail to select transaction info by estate id");
            return null;
        }
    }
}
