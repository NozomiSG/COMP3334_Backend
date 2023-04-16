package com.mybatisplus_comp3334.service.concept;

import com.mybatisplus_comp3334.entity.Estate;

import java.util.List;

public interface EstateService {
    String insertEstateInfo(Estate estate);
    String updateEstateInfo(Estate estate);
    String deleteEstateInfo(Estate estate);
    Estate selectEstateInfoById(Long id);
    List<Estate> selectEstateInfoByOwnerId(Long ownerId);
//    List<Estate> selectAllEstateInfo();
}