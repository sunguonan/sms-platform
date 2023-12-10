package com.nanyuan.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanyuan.sms.dto.ConfigDTO;
import com.nanyuan.sms.entity.ConfigEntity;
import com.nanyuan.sms.mapper.ConfigMapper;
import com.nanyuan.sms.model.ServerTopic;
import com.nanyuan.sms.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 通道配置表
 */
@Service
@Slf4j
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, ConfigEntity> implements ConfigService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ConfigEntity getByName(String name) {
        LambdaUpdateWrapper<ConfigEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ConfigEntity::getName, name);
        return this.getOne(wrapper);
    }

    @Override
    public void getNewLevel(ConfigDTO entity) {
        LambdaUpdateWrapper<ConfigEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ConfigEntity::getIsEnable, 1);
        wrapper.eq(ConfigEntity::getIsActive, 1);
        wrapper.orderByDesc(ConfigEntity::getLevel);
        wrapper.last("limit 1");
        ConfigEntity configEntity = this.getOne(wrapper);
        if (configEntity == null) {
            entity.setLevel(1);
        } else {
            entity.setLevel(configEntity.getLevel() + 1);
        }
    }

    @Override
    public void sendUpdateMessage() {
        // TODO 当通道优先级发生变更,发送消息，通知短信发送服务更新内存中的通道优先级

        // 检查发送端存活情况
        Map<Integer, Long> map = redisTemplate.opsForHash().entries("SERVER_ID_HASH");
        log.info("发送端存活情况 {}", map);

        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();

        for (Map.Entry<Integer, Long> entry : map.entrySet()) {
            // 检查是否有实例小于5min且有上报信息  有上报信息说明 发送端正常
            Long valueLong = entry.getValue();
            if (valueLong - currentTimeMillis < (5 * 60 * 1000)) {
                // 删除旧的通道优先级
                redisTemplate.delete("listForConnect");
                // 通知发送端优先级变更
                ServerTopic serverTopic = ServerTopic.builder().option(ServerTopic.INIT_CONNECT).value(entry.getValue().toString()).build();
                redisTemplate.convertAndSend("TOPIC_HIGH_SERVER", serverTopic);
                log.info("发送消息通知短信发送服务重新构建通道");
                // 使得整个sendUpdateMessage方法全部结束  因为只要通知到一台发送端即可  不需要重复通知发送端  避免造成资源浪费
                return;
            }
        }
    }
}
