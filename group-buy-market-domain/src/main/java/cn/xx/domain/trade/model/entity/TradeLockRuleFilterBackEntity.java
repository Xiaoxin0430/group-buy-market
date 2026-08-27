package cn.xx.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoxin
 * @description 拼团交易，过滤反馈实体
 * @create 2026/8/24 14:20
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeLockRuleFilterBackEntity {
    // 用户参与活动的订单量
    private Integer userTakeOrderCount;
}
