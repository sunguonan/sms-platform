package com.nanyuan.sms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nanyuan.sms.entity.PlatformEntity;

/**
 * 接入平台
 *
 * 
 */
public interface PlatformService extends IService<PlatformEntity> {

    PlatformEntity getByName(String name);
}
