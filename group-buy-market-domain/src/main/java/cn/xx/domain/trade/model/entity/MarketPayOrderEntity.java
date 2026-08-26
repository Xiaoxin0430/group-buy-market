package cn.xx.domain.trade.model.entity;

import cn.xx.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author xiaoxin
 * @description 预支付订单实体
 * @create 2026/8/5 15:42
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketPayOrderEntity {

    /** 预购订单ID */
    private String orderId;

    /** 折扣金额 */
    private BigDecimal deductionPrice;

    /** 交易订单状态枚举 */
    private TradeOrderStatusEnumVO tradeOrderStatusEnumVO;

    /** 拼单组队ID */
    private String teamId;

}
