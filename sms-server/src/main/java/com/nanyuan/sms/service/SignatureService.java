package com.nanyuan.sms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nanyuan.sms.entity.SignatureEntity;

/**
 * 签名表
 *
 * 
 */
public interface SignatureService extends IService<SignatureEntity> {

    SignatureEntity getByCode(String signature);

    String getConfigCodeByCode(String id, String signature);
}
