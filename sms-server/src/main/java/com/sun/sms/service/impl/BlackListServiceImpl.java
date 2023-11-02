package com.sun.sms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.sms.entity.BlackListEntity;
import com.sun.sms.mapper.BlackListMapper;
import com.sun.sms.service.BlackListService;
import org.springframework.stereotype.Service;

/**
 * 黑名单
 *
 * @author IT李老师
 */
@Service
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackListEntity> implements BlackListService {


}
