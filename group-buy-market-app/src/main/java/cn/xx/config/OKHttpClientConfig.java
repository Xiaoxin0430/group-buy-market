package cn.xx.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/28 16:43
 */


@Configuration
public class OKHttpClientConfig {

    @Bean
    public OkHttpClient httpClient() {
        return new OkHttpClient();
    }

}
