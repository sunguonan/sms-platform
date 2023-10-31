package com.sun.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.sms.entity.BlackListEntity;
import com.sun.sms.mapper.BlackListMapper;
import com.sun.sms.service.BlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 黑名单
 *
 * @author IT李老师
 */
@Service
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackListEntity> implements BlackListService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<String> listByType(String type) {
        // 从缓存中拿信息
        ValueOperations<String, List<String>> ops = redisTemplate.opsForValue();
        List<String> phones = ops.get("Black_" + type);
        // 如果从Redis中拿不到信息 去数据库中查询 查询出的结果放在Redis中
        if (CollectionUtils.isEmpty(phones)) {
            LambdaQueryWrapper<BlackListEntity> wrapper = new LambdaQueryWrapper();
            wrapper.eq(BlackListEntity::getType, type);
            List<BlackListEntity> blackListEntities = baseMapper.selectList(wrapper);
            phones = blackListEntities.stream().map(item -> item.getContent()).collect(Collectors.toList());
            if (null == phones) {
                phones = Collections.emptyList();
            }
            // 存入到Redis缓存中
            ops.set("Black_" + type, phones, 60, TimeUnit.SECONDS);
        }
        return phones;
    }
}