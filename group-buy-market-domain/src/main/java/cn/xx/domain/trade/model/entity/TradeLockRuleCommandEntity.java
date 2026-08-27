package cn.xx.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoxin
 * @description 拼团交易规则命令实体
 * @create 2026/8/24 14:14
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeLockRuleCommandEntity {
    //检查谁参加哪个活动？

    /** 用户ID */
    private String userId;

    /** 活动ID */
    private Long activityId;
}
