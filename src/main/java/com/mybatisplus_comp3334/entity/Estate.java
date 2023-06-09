package com.mybatisplus_comp3334.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estate {

    @TableId(type = IdType.AUTO)
    private Long estateId;

    private String estateName;

    private String estateDescription;

    private Long estateOwnerId;

    private Integer estatePrice;

    private Boolean estateStatus;

}
