package com.nanyuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanyuan.service.TemplateService;
import com.nanyuan.sms.entity.TemplateEntity;
import com.nanyuan.sms.mapper.TemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 短信模板
 *
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, TemplateEntity> implements TemplateService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public TemplateEntity getByCode(String template) {
        ValueOperations<String, TemplateEntity> ops = redisTemplate.opsForValue();
        TemplateEntity templateEntity = ops.get(template);
        if (templateEntity == null) {
            LambdaQueryWrapper<TemplateEntity> wrapper = new LambdaQueryWrapper();
            wrapper.eq(TemplateEntity::getCode, template);
            templateEntity = baseMapper.selectOne(wrapper);
            ops.set(template, templateEntity, 60, TimeUnit.SECONDS);
        }
        return templateEntity;
    }
}
