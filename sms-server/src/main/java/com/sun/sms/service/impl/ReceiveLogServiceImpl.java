package com.sun.sms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.sms.entity.ReceiveLogEntity;
import com.sun.sms.mapper.ReceiveLogMapper;
import com.sun.sms.service.ReceiveLogService;
import org.springframework.stereotype.Service;

/**
 * 接收日志表
 *
 * @author IT李老师
 */
@Service
public class ReceiveLogServiceImpl extends ServiceImpl<ReceiveLogMapper, ReceiveLogEntity> implements ReceiveLogService {

}
