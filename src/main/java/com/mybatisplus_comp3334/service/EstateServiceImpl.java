package com.mybatisplus_comp3334.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus_comp3334.entity.Estate;
import com.mybatisplus_comp3334.mapper.EstateMapper;
import com.mybatisplus_comp3334.service.concept.EstateService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log
@Service
@Transactional
public class EstateServiceImpl implements EstateService {

    @Autowired
    private EstateMapper estateMapper;

    @Override
    public String insertEstateInfo(Estate estate) {
        log.info("Insert estate info");
        if (estateMapper.insert(estate)>=0) {
            log.info("Success to insert estate info");
            return "Success";
        } else {
            log.info("Fail to insert estate info");
            return "Fail";
        }
    }

    @Override
    public String updateEstateInfo(Estate estate) {
        log.info("Update estate info");
        if (estateMapper.updateById(estate)>=0) {
            log.info("Success to update estate info");
            return "Success";
        } else {
            log.info("Fail to update estate info");
            return "Fail";
        }
    }

    @Override
    public String deleteEstateInfo(Estate estate) {
        log.info("Delete estate info");
        if (estateMapper.deleteById(estate.getEstateId())>=0) {
            log.info("Success to delete estate info");
            return "Success";
        } else {
            log.info("Fail to delete estate info");
            return "Fail";
        }
    }

    @Override
    public Estate selectEstateInfoById(Long id) {
        log.info("Select estate info by id");
        if (estateMapper.selectById(id) != null) {
            log.info("Success to select estate info by id");
            return estateMapper.selectById(id);
        } else {
            log.info("Fail to select estate info by id");
            return null;
        }
    }

    @Override
    public List<Estate> selectEstateInfoByOwnerId(Long ownerId) {
        log.info("Select estate info by user id");
        estateMapper.selectList(new QueryWrapper<>(new Estate()).eq("estate_owner_id", ownerId));
        if (estateMapper.selectList(new QueryWrapper<>(new Estate()).eq("estate_owner_id", ownerId)) != null) {
            log.info("Success to select estate info by user id");
            return estateMapper.selectList(new QueryWrapper<>(new Estate()).eq("estate_owner_id", ownerId));
        } else {
            log.info("Fail to select estate info by user id");
            return null;
        }
    }


}
