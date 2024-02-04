package com.nanyuan.sms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanyuan.sms.entity.BlackListEntity;
import com.nanyuan.sms.mapper.BlackListMapper;
import com.nanyuan.sms.service.BlackListService;
import org.springframework.stereotype.Service;

/**
 * 黑名单
 */
@Service
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackListEntity> implements BlackListService {


}
