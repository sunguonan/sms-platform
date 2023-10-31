package com.sun.sms.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.sms.entity.BlackListEntity;
import org.springframework.stereotype.Repository;

/**
 * 黑名单
 */
@Repository
public interface BlackListMapper extends BaseMapper<BlackListEntity> {

}
