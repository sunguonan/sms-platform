package com.nanyuan.sms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nanyuan.sms.entity.ReceiveLogEntity;
import com.nanyuan.sms.vo.ReceiveLogVO;
import com.nanyuan.sms.vo.StatisticsCountVO;

import java.util.List;
import java.util.Map;

/**
 * 接收日志表
 *
 * 
 */
public interface ReceiveLogService extends IService<ReceiveLogEntity> {

    Page<ReceiveLogVO> pageLog(Page<ReceiveLogVO> page, Map<String, Object> params);

    List<StatisticsCountVO> top10(Map params);

    List<StatisticsCountVO> trend(Map params);
}
