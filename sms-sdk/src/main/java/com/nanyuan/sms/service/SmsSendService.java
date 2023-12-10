package com.nanyuan.sms.service;


import com.nanyuan.sms.dto.R;
import com.nanyuan.sms.dto.SmsBatchParamsDTO;
import com.nanyuan.sms.dto.SmsParamsDTO;

public interface SmsSendService {
    R sendSms(SmsParamsDTO smsParamsDTO);

    R batchSendSms(SmsBatchParamsDTO smsBatchParamsDTO);
}
