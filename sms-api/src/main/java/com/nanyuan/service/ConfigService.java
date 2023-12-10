package com.nanyuan.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nanyuan.sms.entity.ConfigEntity;

import java.util.List;

/**
 * 配置表
 *
 * @author IT李老师
 */
public interface ConfigService extends IService<ConfigEntity> {

    List<ConfigEntity> findByTemplateSignature(String template, String signature);
}
