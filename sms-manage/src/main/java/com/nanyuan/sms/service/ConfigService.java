package com.nanyuan.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nanyuan.sms.dto.ConfigDTO;
import com.nanyuan.sms.entity.ConfigEntity;

/**
 * 配置表
 */
public interface ConfigService extends IService<ConfigEntity> {

    ConfigEntity getByName(String name);

    void getNewLevel(ConfigDTO entity);

    void sendUpdateMessage();
}
