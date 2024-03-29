package com.nanyuan.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nanyuan.sms.dto.ConfigDTO;
import com.nanyuan.sms.entity.ConfigTemplateEntity;

/**
 * 配置—模板表
 *
 * 
 */
public interface ConfigTemplateService extends IService<ConfigTemplateEntity> {

    void merge(ConfigDTO entity);
}
