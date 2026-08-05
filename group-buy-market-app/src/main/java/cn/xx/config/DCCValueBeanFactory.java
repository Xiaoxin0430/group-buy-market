package cn.xx.config;

/**
 * @author xiaoxin
 * @description 实现redis配置监听与热更新
 * @create 2026/8/4 19:05
 */


import cn.xx.types.annotations.DCCValue;
import cn.xx.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于 Redis 实现动态配置中心
 */
@Slf4j
@Configuration
public class DCCValueBeanFactory implements BeanPostProcessor {

    /**
     * Redis 中 DCC 配置的统一前缀
     */
    private static final String BASE_CONFIG_PATH =
            "group_buy_market_dcc_";

    /**
     * Redis 操作客户端
     */
    private final RedissonClient redissonClient;

    /**
     * 保存配置 Key 和对应的 Spring Bean
     *
     * 下一项配置热更新时，会通过这个 Map
     * 找到需要被修改的对象。
     */
    private final Map<String, Object> dccObjGroup =
            new HashMap<>();

    public DCCValueBeanFactory(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public Object postProcessAfterInitialization(
            Object bean,
            String beanName) throws BeansException {

        /*
         * 通过反射获取真正需要扫描的类和对象。
         *
         * 普通 Bean 可以直接使用 bean.getClass()。
         * 但经过 Spring AOP 代理后，bean 可能是代理对象，必须找到代理背后的原始类和原始对象。
         */
        Class<?> targetBeanClass = bean.getClass();
        Object targetBeanObject = bean;

        if (AopUtils.isAopProxy(bean)) {
            targetBeanClass = AopUtils.getTargetClass(bean);
            targetBeanObject =
                    AopProxyUtils.getSingletonTarget(bean);
        }

        /*
         * 获取当前类中声明的全部字段。
         */
        Field[] fields = targetBeanClass.getDeclaredFields();

        for (Field field : fields) {

            /*
             * 没有 @DCCValue 注解的字段直接跳过。
             */
            if (!field.isAnnotationPresent(DCCValue.class)) {
                continue;
            }

            /*
             * 获取字段上的 @DCCValue 注解。
             */
            DCCValue dccValue = field.getAnnotation(DCCValue.class);

            /*
             * 获取注解中的配置内容。
             *
             * 例如：
             * value = downgradeSwitch:0
             */
            String value = dccValue.value();

            if (StringUtils.isBlank(value)) {
                throw new RuntimeException(
                        field.getName()
                                + " @DCCValue is not config value "
                                + "config case 「isSwitch/isSwitch:1」"
                );
            }

            /*
             * 按冒号拆分配置。
             *
             * downgradeSwitch:0
             *
             * splits[0] = downgradeSwitch
             * splits[1] = 0
             */
            String[] splits = value.split(":");

            /*
             * 生成 Redis Key：
             *
             * group_buy_market_dcc_downgradeSwitch
             */
            String key =
                    BASE_CONFIG_PATH.concat(splits[0]);

            /*
             * 获取默认值。
             */
            String defaultValue =
                    splits.length == 2 ? splits[1] : null;

            /*
             * 默认情况下，准备把默认值设置到字段中。
             *
             * 如果 Redis 中已经存在配置，
             * 后面会把 setValue 替换成 Redis 中的值。
             */
            String setValue = defaultValue;

            try {
                /*
                 * 所有 DCC 配置必须提供默认值。
                 */
                if (StringUtils.isBlank(defaultValue)) {
                    throw new RuntimeException(
                            "dcc config error "
                                    + key
                                    + " is not null - 请配置默认值！"
                    );
                }

                /*
                 * 获取 Redis 中的字符串 Bucket。
                 */
                RBucket<String> bucket =
                        redissonClient.getBucket(key);

                /*
                 * 判断 Redis 中是否已经存在该配置。
                 */
                boolean exists = bucket.isExists();

                if (!exists) {
                    /*
                     * Redis 不存在：
                     * 将注解中的默认值写入 Redis。
                     */
                    bucket.set(defaultValue);
                } else {
                    /*
                     * Redis 已存在：
                     * 使用 Redis 中的最新值。
                     */
                    setValue = bucket.get();
                }

                /*
                 * private 字段默认不能从外部直接修改，
                 * 所以临时开启访问权限。
                 */
                field.setAccessible(true);

                /*
                 * 将配置值写入真正的 Bean 对象。
                 */
                field.set(targetBeanObject, setValue);

                /*
                 * 修改完成后关闭访问权限。
                 */
                field.setAccessible(false);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            /*
             * 保存 Redis Key 和对应对象的关系。
             *
             * 下一项收到动态配置消息时，
             * 可以根据 Key 找到这个对象并重新修改字段。
             */
            dccObjGroup.put(key, targetBeanObject);
        }

        /*
         * BeanPostProcessor 必须把原来的 Bean 返回。
         * Spring 后续还要继续使用这个对象。
         */
        return bean;
    }

    //redis topic监听，收到消息实现配置热更新
    @Bean("dccTopic")
    public RTopic dccRedisTopicListener(
            RedissonClient redissonClient) {

        RTopic topic = redissonClient.getTopic("group_buy_market_dcc");

        topic.addListener(String.class, (charSequence, s) -> {

                    String[] split = s.split(Constants.SPLIT);

                    // 获取值
                    String attribute = split[0];
                    String key = BASE_CONFIG_PATH + attribute;
                    String value = split[1];

                    // 更新redis值
                    RBucket<String> bucket = redissonClient.getBucket(key);

                    boolean exists = bucket.isExists();

                    if (!exists) {
                        return;
                    }

                    bucket.set(value);

                    //更新Java对象的值
                    Object objBean = dccObjGroup.get(key);

                    if (null == objBean) {
                        return;
                    }

                    Class<?> objBeanClass = objBean.getClass();

                    // 检查 objBean 是否是代理对象
                    if (AopUtils.isAopProxy(objBean)) {
                        // 获取代理对象的目标类
                        objBeanClass = AopUtils.getTargetClass(objBean);
                    }

                    try {
                        /*
                         * getDeclaredField：
                         * 获取当前类自己声明的字段，
                         * 包括 private、protected、public 字段。
                         */
                        Field field = objBeanClass.getDeclaredField(attribute);

                        field.setAccessible(true);
                        field.set(objBean, value);
                        field.setAccessible(false);

                        log.info(
                                "DCC 节点监听，动态设置值 {} {}",
                                key,
                                value
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        return topic;
    }

}