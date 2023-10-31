package com.sun.sms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.sms.entity.TemplateEntity;

/**
 * 模板表
 *
 * @author IT李老师
 */
public interface TemplateService extends IService<TemplateEntity> {

    TemplateEntity getByCode(String template);
}
