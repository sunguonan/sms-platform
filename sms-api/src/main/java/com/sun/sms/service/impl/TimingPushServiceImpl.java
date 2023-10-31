package com.sun.sms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.sms.entity.TimingPushEntity;
import com.sun.sms.mapper.TimingPushMapper;
import com.sun.sms.service.TimingPushService;
import org.springframework.stereotype.Service;

/**
 * 定时发送
 *
 * @author IT李老师
 */
@Service
public class TimingPushServiceImpl extends ServiceImpl<TimingPushMapper, TimingPushEntity> implements TimingPushService {

}
