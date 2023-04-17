package com.mybatisplus_comp3334.service.concept;

import com.mybatisplus_comp3334.entity.Transaction;

import java.util.List;

public interface TransactionService {
    String insertTransactionInfo(Transaction TransRecord);
    Transaction selectTransactionInfoByTransId(Long transId);
    List<Transaction> selectTransactionInfoByBuyerId(Long BuyerId);
    List<Transaction> selectTransactionInfoBySellerId(Long SellerId);
    List<Transaction> selectTransactionInfoByEstateId(Long EstateId);
    Transaction selectUndoneTransactionByEstateId(Long EstateId);
    String deleteUndoneTransactionByEstateId(Long EstateId);
}
