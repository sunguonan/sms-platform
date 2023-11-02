package com.sun.sms.factory;

import com.alibaba.fastjson.JSON;
import com.sun.sms.config.RedisLock;
import com.sun.sms.entity.ConfigEntity;
import com.sun.sms.entity.SmsConfig;
import com.sun.sms.model.ServerTopic;
import com.sun.sms.service.ConfigService;
import com.sun.sms.service.SignatureService;
import com.sun.sms.service.TemplateService;
import com.ydl.utils.SpringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 通道实例加载器
 * 执行时间：
 * 1、项目启动时
 * 2、通道重新排序时
 * 执行逻辑
 * 1. 从数据库中获取通道列表 （如果有才通过反射构造对象，没有就不new了）
 * 2. 遍历通道列表    （A通道）。。。
 * 2.1 构造smsConfig对象
 * 2.2 反射创建service
 * 3. 将bean对象保存到数组中
 */
@Component
@Slf4j
@Order(value = 101)
public class SmsConnectLoader implements CommandLineRunner {

    private static final List<Object> CONNECT_LIST = new ArrayList<>();

    private static String BUILD_NEW_CONNECT_TOKEN = null;

    private static List<ConfigEntity> FUTURE_CONFIG_LIST;

    @Autowired
    private ConfigService configService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void run(String... args) {
        initConnect();
    }

    /**
     * 通道初始化
     * 根据通道配置，初始化每个通道的bean对象
     */
    @SneakyThrows
    public void initConnect() {
        // TODO 根据通道配置，初始化每个通道的bean对象

        // 1、查询数据库获得通道列表
        List<ConfigEntity> configEntities = configService.listForConnect();

        ArrayList<Object> list = new ArrayList<>();

        configEntities.forEach(configEntity -> {
            try {
                // 因为反射出实例对象的时候需要一个smsConfig的一个参数  所以new出一个smsConfig对象 填入参数
                SmsConfig smsConfig = new SmsConfig();
                smsConfig.setId(configEntity.getId());
                smsConfig.setDomain(configEntity.getDomain());
                smsConfig.setAccessKeyId(configEntity.getAccessKeyId().trim());
                smsConfig.setAccessKeySecret(configEntity.getAccessKeySecret().trim());
                smsConfig.setName(configEntity.getName().trim());
                smsConfig.setPlatform(configEntity.getPlatform().trim());
                // 当其他参数不为空的时候
                if (StringUtils.isNotBlank(configEntity.getOther())) {
                    // 在获取ConfigEntity中的Other参数已经转换为json字符串  此时我们只需要转成一个map即可
                    LinkedHashMap linkedHashMap = JSON.parseObject(configEntity.getOther(), LinkedHashMap.class);
                    smsConfig.setOtherConfig(linkedHashMap);
                }

                // 反射 创建Service
                // 全限定名 com.ydl.sms.sms.AliyunSmsService
                String className = "com.ydl.sms.sms." + configEntity.getPlatform() + "SmsService";

                // 2、遍历通道列表，通过反射创建每个通道的Bean对象（例如AliyunSmsService、MengWangSmsService等）
                // 加载类
                Class<?> aClass = Class.forName(className);
                // 拿到构造方法
                Constructor<?> constructor = aClass.getConstructor(SmsConfig.class);
                // 创建对象
                Object obj = constructor.newInstance(smsConfig);

                // 从容器中获取签名和模板的service
                SignatureService signatureService = SpringUtils.getBean(SignatureService.class);
                TemplateService templateService = SpringUtils.getBean(TemplateService.class);

                // 找到这两个service在父类中的属性
                Field signatureServiceField = aClass.getSuperclass().getDeclaredField("signatureService");
                Field templateServiceField = aClass.getSuperclass().getDeclaredField("templateService");
                // 打开访问权限  暴力破解
                signatureServiceField.setAccessible(true);
                templateServiceField.setAccessible(true);
                // 设置属性值了
                signatureServiceField.set(obj, signatureService);
                templateServiceField.set(obj, templateService);

                // 将实例化成功的对象放进一个列表中  不要直接放进CONNECT_LIST中，可能当前的CONNECT_LIST不为空
                list.add(obj);

                log.info("初始化通道成功：{}，{}", smsConfig.getName(), smsConfig.getPlatform());
            } catch (Exception e) {
                log.warn("初始化通道失败{}", e.getMessage());
            }
        });

        // 3、将每个通道的Bean对象保存到CONNECT_LIST集合中
        if (!CONNECT_LIST.isEmpty()) {
            // 如果CONNECT_LIST不为空  则清空列表
            CONNECT_LIST.clear();
        }
        // 再重新添加到列表中
        CONNECT_LIST.addAll(list);

        // 解锁逻辑
        if (StringUtils.isNotBlank(BUILD_NEW_CONNECT_TOKEN)) {
            redisLock.unlock("buildNewConnect", BUILD_NEW_CONNECT_TOKEN);
        }

        log.info("通道初始化完成了。{}", CONNECT_LIST);
    }

    public <T> T getConnectByLevel(Integer level) {
        return (T) CONNECT_LIST.get(level - 1);
    }

    /**
     * 返回可用通道的个数是否小于需要发送通道的优先级
     * CONNECT_LIST.size()  可用通道 1
     * level 需要发送通道的优先级  2
     * 1 < 2  true  则表示发送失败
     *
     * @param level
     * @return
     */
    public boolean checkConnectLevel(Integer level) {
        return CONNECT_LIST.size() < level;
    }

    /**
     * 通道调整：
     * 通道初始化：构建新的通道配置
     * 只能有一台机器执行，所以需要加锁
     */
    public void buildNewConnect() {
        // 一小时内有效
        String token = redisLock.tryLock("buildNewConnect", 1000 * 60 * 60 * 1);
        log.info("buildNewConnect token:{}", token);
        if (StringUtils.isNotBlank(token)) {
            List<ConfigEntity> list = configService.listForNewConnect();
            FUTURE_CONFIG_LIST = list;
            redisTemplate.opsForValue().set("NEW_CONNECT_SERVER", ServerRegister.SERVER_ID);
            BUILD_NEW_CONNECT_TOKEN = token;
        }
        // 获取不到锁 证明已经有服务在计算或者计算结果未得到使用
    }

    /**
     * 通道调整：
     * 发布订阅消息，通知其他服务：应用新的通道
     */
    public void changeNewConnectMessage() {
        redisTemplate.convertAndSend("TOPIC_HIGH_SERVER",
                ServerTopic.builder().option(ServerTopic.USE_NEW_CONNECT)
                        .value(ServerRegister.SERVER_ID).build().toString());
    }

    /**
     * 通道调整
     * 发布订阅消息，通知其他服务：初始化新通道
     */
    public void changeNewConnect() {
        // 初始化通道
        Object newConnectServer = redisTemplate.opsForValue().get("NEW_CONNECT_SERVER");

        /**
         * 为了通道调整发布的消息中，带有server id
         * 确保只有此server id的服务执行当前代码
         */
        if (null != newConnectServer && ServerRegister.SERVER_ID.equals(newConnectServer) &&
                !CollectionUtils.isEmpty(FUTURE_CONFIG_LIST)) {
            // 配置列表不为空则执行数据库操作 并清空缓存
            boolean result = configService.updateBatchById(FUTURE_CONFIG_LIST);
            log.info("批量修改配置级别:{}", result);
            FUTURE_CONFIG_LIST.clear();
            redisTemplate.convertAndSend("TOPIC_HIGH_SERVER", ServerTopic.builder().option(ServerTopic.INIT_CONNECT).value(ServerRegister.SERVER_ID).build().toString());
        }
    }
}
