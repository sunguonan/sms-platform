package com.sun.sms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.sms.entity.ConfigTemplateEntity;
import com.sun.sms.mapper.ConfigTemplateMapper;
import com.sun.sms.service.ConfigTemplateService;
import org.springframework.stereotype.Service;

/**
 * 配置—模板表
 *
 * @author IT李老师
 */
@Service
public class ConfigTemplateServiceImpl extends ServiceImpl<ConfigTemplateMapper, ConfigTemplateEntity> implements ConfigTemplateService {

}
