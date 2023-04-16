package com.mybatisplus_comp3334.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRecord {

    @TableId(type = IdType.AUTO)
    private Long transId;

    private Long estateId;

    private Long sellerId;

    private Long buyerId;

    private java.sql.Timestamp transactionTime;

}
