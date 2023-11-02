package com.sun.sms.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * 服务注册器，将短信发送服务注册到Redis中，定时服务上报，定时服务检查
 * 1. 服务启动的时候，将服务id注册到Redis中
 * 2. 定时任务： 每三分钟定时上报信息  （刷新时间戳）
 * 3. 定时任务： 每隔十分钟查看，超过五分钟还没上报的服务
 * 如果超过时间还没上报自己的信息，则删除自己的信息
 */
@Component
@Slf4j
@Order(value = 100)
public class ServerRegister implements CommandLineRunner {
    // 当前服务实例的唯一标识，可以使用UUID随机生成
    public static String SERVER_ID = null;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 项目启动时自动执行此方法，将当前服务实例注册到redis
     *
     * @param args
     */
    @Override
    public void run(String... args) {
        // TODO 服务注册器，项目启动时将当前服务id注册到Redis中，使用Redis的Hash结构，key为SERVER_ID_HASH，Hash结构的key为服务id，value为时间戳
        // SERVER_ID_HASH   key  value
        // 生成UUID  key
        SERVER_ID = UUID.randomUUID().toString();
        // 生成当前时间戳 value
        long timeMillisValue = System.currentTimeMillis();
        log.info("服务启动成功,注册到Redis的id是{}", SERVER_ID);

        // 存入Redis中
        redisTemplate.opsForHash().put("SERVER_ID_HASH", SERVER_ID, timeMillisValue);
    }

    /**
     * 定时服务报告
     * 报告服务信息证明服务存在 每三分钟报告一次，并传入当前时间戳
     */
    @Scheduled(cron = "1 1/3 * * * ?")
    public void serverReport() {
        // TODO 服务注册器，每三分钟报告一次，并传入当前时间戳
        log.info("服务定时上报。id:{}", SERVER_ID);
        // 生成当前时间戳 value
        long timeMillisValue = System.currentTimeMillis();
        redisTemplate.opsForHash().put("SERVER_ID_HASH", SERVER_ID, timeMillisValue);
    }

    /**
     * 定时服务检查
     * 每十分钟检查一次服务列表，清空超过五分钟没有报告的服务
     */
    @Scheduled(cron = "30 1/10 * * * ?")
    public void checkServer() {
        // TODO 服务注册器，定时检查redis,每隔10分钟查看，超过5分钟还没上报自己信息的服务，清除掉

        log.info("定时服务检查。id:{}", SERVER_ID);
        // 拿出Redis中的hash的键 
        Map<Object, Object> map = redisTemplate.opsForHash().entries("SERVER_ID_HASH");
        log.info("当前服务有：" + map);

        // 获取当前时间
        long nowTime = System.currentTimeMillis();
        // 暂存需要删除的key
        ArrayList<String> arr = new ArrayList<>();

        map.forEach((key, value) -> {
            long registerTime = Long.parseLong(value.toString());
            // 如果当前时间减去注册的时间（说明服务失去链接时间） 大于 5分钟
            if (nowTime - registerTime > (5 * 60 * 1000)) {
                arr.add(key.toString());
            }
        });

        log.info("该要删除的key有：{}", arr);
        // 删除过期的key
        arr.forEach(key -> redisTemplate.opsForHash().delete("SERVER_ID_HASH", key));
    }
}

