package com.nanyuan.sms.dto;

import com.nanyuan.sms.entity.ManualProcessEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 人工处理任务表
 *
 * 
 */
@Data
@ApiModel(description = "人工处理任务表")
public class ManualProcessDTO extends ManualProcessEntity {

}
