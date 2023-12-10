package com.nanyuan.sms.service.impl;

import com.alibaba.fastjson.JSON;
import com.nanyuan.sms.dto.BaseParamsDTO;
import com.nanyuan.sms.dto.R;
import com.nanyuan.sms.dto.SmsBatchParamsDTO;
import com.nanyuan.sms.dto.SmsParamsDTO;
import com.nanyuan.sms.service.SmsSendService;
import com.nanyuan.sms.utils.SmsEncryptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SmsSendServiceImpl implements SmsSendService {
    @Value("${ydlclass.sms.auth}")
    private boolean auth;
    @Value("${ydlclass.sms.domain}")
    private String domain;
    @Value("${ydlclass.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${ydlclass.sms.accessKeySecret}")
    private String accessKeySecret;

    private String send = "/sms/send";
    private String batchSend = "/sms/batchSend";

    /**
     * 单条短信
     *
     * @param smsParamsDTO
     * @return
     */
    @Override
    public R sendSms(SmsParamsDTO smsParamsDTO) {
        String url = domain + send;
        return send(smsParamsDTO, url);
    }

    /**
     * 批量短信
     *
     * @param smsBatchParamsDTO
     * @return
     */
    @Override
    public R batchSendSms(SmsBatchParamsDTO smsBatchParamsDTO) {
        String url = domain + batchSend;
        return send(smsBatchParamsDTO, url);
    }

    /**
     * 通过HttpClient发送post请求，请求短信接收服务HTTP接口
     *
     * @param baseParamsDTO
     * @param url
     * @return
     */
    private R send(BaseParamsDTO baseParamsDTO, String url) {

        // 判断是否需要认证  如果设置不需要认证，会直接发送到短信接收端
        if (auth) {
            // 需要认证
            if (StringUtils.isBlank(accessKeyId) || StringUtils.isBlank(accessKeySecret)) {
                return R.fail("accessKeyId或accessKeySecret不能为空");
            }
        }

        // 设置该平台秘钥
        baseParamsDTO.setAccessKeyId(accessKeyId);

        // 设置认证值
        baseParamsDTO.setEncryption(SmsEncryptionUtils.encode(baseParamsDTO.getTimestamp(), baseParamsDTO.getAccessKeyId(), accessKeySecret));
        // 设置发送时间戳
        baseParamsDTO.setTimestamp(String.valueOf(System.currentTimeMillis()));


        // 判断domain是否为空
        if (StringUtils.isBlank(domain)) {
            // 如果为空
            return R.fail("domain不能为空");
        }

        // httpClient 发送请求
        CloseableHttpClient client = HttpClients.createDefault();
        // 构造post请求  放入url
        HttpPost post = new HttpPost(url);
        // 设置请求头
        post.setHeader("Content-Type", "application/json; charset=UTF-8");
        // 配置请求体
        StringEntity stringEntity = new StringEntity(JSON.toJSONString(baseParamsDTO), "UTF-8");
        post.setEntity(stringEntity);

        try {
            // 发送请求 并返回响应
            CloseableHttpResponse response = client.execute(post);
            // 获取响应体
            HttpEntity entity = response.getEntity();

            if (response.getStatusLine().getStatusCode() == 200) {
                // 发送成功
                log.info("httpRequest access success, StatusCode is:{}", response.getStatusLine().getStatusCode());
                String responseEntityStr = EntityUtils.toString(entity);
                log.info("responseContent is :" + responseEntityStr);
                // 从短信的接收端返回的就是R这个对象   只要再用json转换即可
                return JSON.parseObject(responseEntityStr, R.class);
            } else {
                // 发送失败
                log.error("httpRequest access fail ,StatusCode is:{}", response.getStatusLine().getStatusCode());
                return R.fail("status is " + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            log.error("error", e);
            return R.fail(e.getMessage());
        } finally {
            // 关闭连接
            post.releaseConnection();
        }
    }
}
