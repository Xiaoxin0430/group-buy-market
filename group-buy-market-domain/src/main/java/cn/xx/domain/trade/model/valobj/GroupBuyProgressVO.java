package cn.xx.domain.trade.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xiaoxin
 * @description 拼团进度值对象
 * @create 2026/8/5 15:43
 */


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyProgressVO {

    /** 目标数量 */
    private Integer targetCount;

    /** 完成数量 */
    private Integer completeCount;

    /** 锁单数量 */
    private Integer lockCount;

}
