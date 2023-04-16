package com.mybatisplus_comp3334.service.concept;

import com.mybatisplus_comp3334.entity.TransactionRecord;

import java.util.List;

public interface TransactionService {
    String insertTransactionInfo(TransactionRecord TransRecord);
    TransactionRecord selectTransactionInfoByTransId(Long transId);
    List<TransactionRecord> selectTransactionInfoByBuyerId(Long BuyerId);
    List<TransactionRecord> selectTransactionInfoBySellerId(Long SellerId);
    List<TransactionRecord> selectTransactionInfoByEstateId(Long EstateId);

}
