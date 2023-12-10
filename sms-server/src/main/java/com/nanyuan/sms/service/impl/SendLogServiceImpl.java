package com.nanyuan.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanyuan.sms.entity.ReceiveLogEntity;
import com.nanyuan.sms.entity.SendLogEntity;
import com.nanyuan.sms.mapper.ReceiveLogMapper;
import com.nanyuan.sms.mapper.SendLogMapper;
import com.nanyuan.sms.service.SendLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志表
 *
 * @author IT李老师
 */
@Service
public class SendLogServiceImpl extends ServiceImpl<SendLogMapper, SendLogEntity> implements SendLogService {

    @Autowired
    private ReceiveLogMapper receiveLogMapper;

    @Override
    public boolean save(SendLogEntity entity) {

        ReceiveLogEntity receiveLogEntity = new ReceiveLogEntity();
        receiveLogEntity.setStatus(entity.getStatus());
        LambdaUpdateWrapper<ReceiveLogEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ReceiveLogEntity::getApiLogId, entity.getApiLogId());
        receiveLogMapper.update(receiveLogEntity, wrapper);

        return super.save(entity);
    }
}
