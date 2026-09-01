package cn.xx.api.dto;

import lombok.Data;

/**
 * @author xiaoxin
 * @description 商品营销请求对象
 * @create 2026/9/1 19:25
 */

@Data
public class GoodsMarketRequestDTO {
    // 用户ID
    private String userId;
    // 渠道
    private String source;
    // 来源
    private String channel;
    // 商品ID
    private String goodsId;

}
