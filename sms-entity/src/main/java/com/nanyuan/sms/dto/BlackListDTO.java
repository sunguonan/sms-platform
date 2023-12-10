package com.nanyuan.sms.dto;

import com.nanyuan.sms.entity.BlackListEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * 黑名单
 *
 * @author IT李老师
 */
@Data
@ApiModel(description = "黑名单")
public class BlackListDTO extends BlackListEntity {

}
