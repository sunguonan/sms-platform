package com.nanyuan.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nanyuan.sms.entity.TemplateEntity;

/**
 * 模板表
 */
public interface TemplateService extends IService<TemplateEntity> {

    TemplateEntity getByCode(String template);
}
