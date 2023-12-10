package com.nanyuan.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanyuan.service.TimingPushService;
import com.nanyuan.sms.entity.TimingPushEntity;
import com.nanyuan.sms.mapper.TimingPushMapper;
import org.springframework.stereotype.Service;

/**
 * 定时发送
 *
 * @author IT李老师
 */
@Service
public class TimingPushServiceImpl extends ServiceImpl<TimingPushMapper, TimingPushEntity> implements TimingPushService {

}
