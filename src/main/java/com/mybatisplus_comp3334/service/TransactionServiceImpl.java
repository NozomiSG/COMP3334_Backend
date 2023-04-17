package com.mybatisplus_comp3334.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus_comp3334.entity.Transaction;
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
    public String insertTransactionInfo(Transaction TransRecord) {
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
    public Transaction selectTransactionInfoByTransId(Long transId) {
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
    public List<Transaction> selectTransactionInfoByBuyerId(Long BuyerId) {
        log.info("Select transaction info by buyer id");
//        transactionMapper.selectList(new QueryWrapper<>(new Transaction()).eq("buyer_id", BuyerId));
        if (transactionMapper.selectList(new QueryWrapper<>(new Transaction()).eq("buyer_id", BuyerId)) != null) {
            log.info("Success to select transaction info by buyer id");
            return transactionMapper.selectList(new QueryWrapper<>(new Transaction()).eq("buyer_id", BuyerId));
        } else {
            log.info("Fail to select transaction info by buyer id");
            return null;
        }
    }

    @Override
    public List<Transaction> selectTransactionInfoBySellerId(Long SellerId) {
        log.info("Select transaction info by seller id");
//        transactionMapper.selectList(new QueryWrapper<>(new Transaction()).eq("seller_id", SellerId));
        if (transactionMapper.selectList(new QueryWrapper<>(new Transaction()).eq("seller_id", SellerId)) != null) {
            log.info("Success to select transaction info by seller id");
            return transactionMapper.selectList(new QueryWrapper<>(new Transaction()).eq("trans_seller_id", SellerId));
        } else {
            log.info("Fail to select transaction info by seller id");
            return null;
        }
    }

    @Override
    public List<Transaction> selectTransactionInfoByEstateId(Long EstateId) {
        log.info("Select transaction info by estate id");
//        transactionMapper.selectList(new QueryWrapper<>(new Transaction()).eq("trans_estate_id", EstateId));
        if (transactionMapper.selectList(new QueryWrapper<>(new Transaction()).eq("trans_estate_id", EstateId)) != null) {
            log.info("Success to select transaction info by estate id");
            return transactionMapper.selectList(new QueryWrapper<>(new Transaction()).eq("trans_estate_id", EstateId));
        } else {
            log.info("Fail to select transaction info by estate id");
            return null;
        }
    }

    @Override
    public String deleteUndoneTransactionByEstateId(Long EstateId) {
        log.info("Delete undone transaction by estate id");
        if (transactionMapper.delete(new QueryWrapper<>(new Transaction()).eq("trans_estate_id", EstateId).eq("trans_status", false)) >= 0) {
            log.info("Success to delete undone transaction by estate id");
            return "Success";
        } else {
            log.info("Fail to delete undone transaction by estate id");
            return "Fail";
        }
    }

    @Override
    public Transaction selectUndoneTransactionByEstateId(Long EstateId) {
        log.info("Select undone transaction by estate id");
        return transactionMapper.selectOne(new QueryWrapper<>(new Transaction()).eq("trans_estate_id", EstateId).eq("trans_status", false));
    }
}
