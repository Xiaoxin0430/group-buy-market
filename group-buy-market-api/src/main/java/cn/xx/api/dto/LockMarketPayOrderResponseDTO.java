package cn.xx.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author xiaoxin
 * @description 营销支付锁单响应DTO
 * @create 2026/8/5 17:08
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LockMarketPayOrderResponseDTO {

    /** 预购订单ID */
    private String orderId;

    /** 折扣金额 */
    private BigDecimal deductionPrice;

    /** 交易订单状态 */
    private Integer tradeOrderStatus;

}
