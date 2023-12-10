package com.nanyuan.sms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanyuan.sms.entity.ManualProcessEntity;
import com.nanyuan.sms.mapper.ManualProcessMapper;
import com.nanyuan.sms.service.ManualProcessService;
import org.springframework.stereotype.Service;

/**
 * 人工处理任务表
 *
 * @author IT李老师
 */
@Service
public class ManualProcessServiceImpl extends ServiceImpl<ManualProcessMapper, ManualProcessEntity> implements ManualProcessService {

}
