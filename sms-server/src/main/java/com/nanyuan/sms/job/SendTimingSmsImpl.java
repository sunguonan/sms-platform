package com.nanyuan.sms.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nanyuan.sms.entity.TimingPushEntity;
import com.nanyuan.sms.factory.SmsFactory;
import com.nanyuan.sms.mapper.TimingPushMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时短信业务处理器
 * <p>
 * 1. 每分钟触发一次定时任务
 * 2. 为了防止短信重复发送，需要使用分布式锁 释放时间30秒
 * 3. 调用SendTimingSmsImpl发送定时短信
 */
@Component
@Slf4j
public class SendTimingSmsImpl implements SendTimingSms {

    @Autowired
    private TimingPushMapper timingPushMapper;

    @Autowired
    private SmsFactory smsFactory;

    /**
     * 发送定时短信
     *
     * @param timing
     */
    @Override
    @Async
    public void execute(String timing) {// timing格式：yyyy-MM-dd HH:mm  2021-12-25 18:00
        // TODO 查询数据库获取本次需要发送的定时短信，调用短信工厂发送短信
        // 1、查询数据库获取本次需要发送的定时短信
        LambdaQueryWrapper<TimingPushEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TimingPushEntity::getStatus, 0)
                .eq(TimingPushEntity::getTiming, timing)
                .orderByAsc(TimingPushEntity::getCreateTime);
        List<TimingPushEntity> timingPushEntities = timingPushMapper.selectList(queryWrapper);

        log.info("这一批次要发的短信条数是：{}，{}", timing, timingPushEntities.size());

        timingPushEntities.forEach(timingPushEntity -> {
            // 2、调用短信工厂发送短信
            smsFactory.send(timingPushEntity.getRequest());
            // 3、更新短信发送状态为“已处理”
            timingPushEntity.setStatus(1);
            timingPushEntity.setUpdateTime(LocalDateTime.now());
            timingPushEntity.setUpdateUser("admin");
            timingPushMapper.updateById(timingPushEntity);
        });

        log.info("任务执行完毕" + timing);
    }
}
