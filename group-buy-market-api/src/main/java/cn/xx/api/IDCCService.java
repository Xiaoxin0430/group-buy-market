package cn.xx.api;

import cn.xx.api.response.Response;

/**
 * @author xiaoxin
 * @description 动态配置中心
 * @create 2026/8/4 21:04
 */


public interface IDCCService {

    Response<Boolean> updateConfig(String key, String value);

}
