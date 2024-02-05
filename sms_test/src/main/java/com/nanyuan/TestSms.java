package com.nanyuan;

import com.nanyuan.sms.dto.SmsParamsDTO;
import com.nanyuan.sms.service.impl.SmsSendServiceImpl;

/**
 * 测试短信发送
 * @author sunGuoNan
 * @version 1.0
 */
public class TestSms {
    public static void main(String[] args) {
        SmsSendServiceImpl smsSendService = new SmsSendServiceImpl();
        SmsParamsDTO smsParamsDTO = new SmsParamsDTO();
        // TODO 这里需要填入测试发送短信的相关参数
        smsSendService.sendSms(smsParamsDTO);
    }
}
