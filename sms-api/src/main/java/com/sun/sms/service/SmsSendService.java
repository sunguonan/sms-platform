package com.sun.sms.service;


import com.sun.sms.dto.SmsBatchParamsDTO;
import com.sun.sms.dto.SmsParamsDTO;

public interface SmsSendService {
    void send(SmsParamsDTO smsParamsDTO);

    void batchSend(SmsBatchParamsDTO smsBatchParamsDTO);
}
