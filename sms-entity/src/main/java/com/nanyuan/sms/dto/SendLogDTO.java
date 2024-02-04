package com.nanyuan.sms.dto;

import com.nanyuan.sms.entity.SendLogEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * 日志表
 *
 * 
 */
@Data
@ApiModel(description = "日志表")
public class SendLogDTO extends SendLogEntity {

}
