package com.mybatisplus_comp3334.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @TableId(type = IdType.AUTO)
    private Long userId;

    private String userPassword;

    private String userEmail;

    private String privateKey;

    private String publicKey;
}
