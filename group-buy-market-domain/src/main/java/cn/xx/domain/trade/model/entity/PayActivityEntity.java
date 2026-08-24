package cn.xx.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xiaoxin
 * @description 活动实体
 * @create 2026/8/5 15:40
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayActivityEntity {

    /** 拼单组队ID */
    private String teamId;

    /** 活动ID */
    private Long activityId;

    /** 活动名称 */
    private String activityName;

    /** 拼团开始时间 */
    private Date startTime;

    /** 拼团结束时间 */
    private Date endTime;

    /** 目标数量 */
    private Integer targetCount;

}
