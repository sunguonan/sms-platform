package com.sun.sms.service;


import com.sun.sms.dto.R;
import com.sun.sms.dto.SmsBatchParamsDTO;
import com.sun.sms.dto.SmsParamsDTO;

public interface SmsSendService {
    R sendSms(SmsParamsDTO smsParamsDTO);

    R batchSendSms(SmsBatchParamsDTO smsBatchParamsDTO);
}
