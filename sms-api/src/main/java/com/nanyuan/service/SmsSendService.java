package com.nanyuan.service;


import com.nanyuan.dto.SmsBatchParamsDTO;
import com.nanyuan.dto.SmsParamsDTO;

public interface SmsSendService {
    void send(SmsParamsDTO smsParamsDTO);

    void batchSend(SmsBatchParamsDTO smsBatchParamsDTO);
}
