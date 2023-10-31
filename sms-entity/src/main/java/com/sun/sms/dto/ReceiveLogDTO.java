package com.sun.sms.dto;

import com.sun.sms.entity.ReceiveLogEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 接收日志表
 *
 * @author IT李老师
 */
@Data
@ApiModel(description = "接收日志表")
public class ReceiveLogDTO extends ReceiveLogEntity {

}
