package cn.xx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/28 19:42
 */


@Data
@ConfigurationProperties(
        prefix = "redis.sdk.config",
        ignoreInvalidFields = true
)
public class RedisClientConfigProperties {
    /** Redis 地址 */
    private String host;

    /** Redis 端口 */
    private int port;

    /** Redis 密码 */
    private String password;

    /** 最大连接池大小 */
    private int poolSize = 64;

    /** 最小空闲连接数量 */
    private int minIdleSize = 10;

    /** 最大空闲时间，单位毫秒 */
    private int idleTimeout = 10000;

    /** 连接超时时间，单位毫秒 */
    private int connectTimeout = 10000;

    /** 重试次数 */
    private int retryAttempts = 3;

    /** 重试间隔，单位毫秒 */
    private int retryInterval = 1000;

    /** Ping 检查间隔，单位毫秒 */
    private int pingInterval = 0;

    /** 是否保持长连接 */
    private boolean keepAlive = true;
}
