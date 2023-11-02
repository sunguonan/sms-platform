package com.sun.sms.job;


import com.sun.sms.config.RedisLock;
import com.ydl.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 定时任务，用于发送定时短信
 */
@Component
@Slf4j
public class SendSmsJob {

    @Autowired
    private SendTimingSms sendTimingSms;

    @Autowired
    private RedisLock redisLock;

    /**
     * 每分钟检查一次是否有定时短信需要发送
     *
     * @throws InterruptedException
     */
    // 1、每分钟触发一次定时任务
    @Scheduled(cron = "10 0/1 * * * ?")     // 每分钟的第10秒执行一次
    public void sendTimingSms() throws InterruptedException {
        // TODO 定时任务，每分钟检查一次是否有定时短信需要发送
        // 添加分布式锁的目的是  比如十个发送端扫表 只能有一个发送端进行发送数据  防止重复发送短信

        // 1、为了防止短信重复发送，需要使用分布式锁 如果能锁住返回的是token值
        String lock = redisLock.tryLock("SEND_TIMING_SMS", 30 * 1000);

        if (StringUtils.isNotBlank(lock)) {
            // 分布式锁生效
            // 2、调用SendTimingSmsImpl发送定时短信
            sendTimingSms.execute(DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm"));
        }
    }
}
