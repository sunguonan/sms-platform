package com.sun.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.sms.entity.PlatformEntity;
import com.sun.sms.mapper.PlatformMapper;
import com.sun.sms.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 平台
 *
 * @author IT李老师
 */
@Service
public class PlatformServiceImpl extends ServiceImpl<PlatformMapper, PlatformEntity> implements PlatformService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取认证的key
     *
     * @param accessKeyId
     * @return
     */
    @Override
    public PlatformEntity getByAccessKeyId(String accessKeyId) {

        ValueOperations<String, PlatformEntity> ops = redisTemplate.opsForValue();
        PlatformEntity platform = ops.get(accessKeyId);
        // 从Redis中拿一下认证的key
        if (platform == null) {
            LambdaQueryWrapper<PlatformEntity> wrapper = new LambdaQueryWrapper();
            wrapper.eq(PlatformEntity::getAccessKeyId, accessKeyId);
            // 去数据库中查询
            platform = baseMapper.selectOne(wrapper);
            // 查询到的platform直接存放在Redis中 并设置60秒的过期时间
            ops.set(accessKeyId, platform, 60, TimeUnit.SECONDS);
        }
        return platform;
    }
}
