package com.nanyuan.sms.dto;

import com.nanyuan.sms.entity.TimingPushEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 定时发送
 *
 * 
 */
@Data
@ApiModel(description = "定时发送")
public class TimingPushDTO extends TimingPushEntity {

}
