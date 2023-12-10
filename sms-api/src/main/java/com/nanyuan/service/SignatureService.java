package com.nanyuan.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nanyuan.sms.entity.SignatureEntity;

/**
 * 签名表
 *
 * @author IT李老师
 */
public interface SignatureService extends IService<SignatureEntity> {

    SignatureEntity getByCode(String signature);
}
