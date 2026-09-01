package cn.xx.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xiaoxin
 * @description 拼团组队实体对象
 * @create 2026/9/1 20:29
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupBuyOrderDetailEntity {
    /** 用户 ID：来自 group_buy_order_list.user_id */
    private String userId;

    /** 拼单组队 ID：两张表关联字段 */
    private String teamId;

    /** 活动 ID：来自 group_buy_order.activity_id */
    private Long activityId;

    /** 成团目标人数：来自 group_buy_order.target_count */
    private Integer targetCount;

    /** 已完成支付人数：来自 group_buy_order.complete_count */
    private Integer completeCount;

    /** 已锁单人数：来自 group_buy_order.lock_count */
    private Integer lockCount;

    /** 队伍开始时间：来自 group_buy_order.valid_start_time */
    private Date validStartTime;

    /** 队伍结束时间：来自 group_buy_order.valid_end_time */
    private Date validEndTime;

    /** 外部支付单号：来自 group_buy_order_list.out_trade_no */
    private String outTradeNo;
}
