package cn.xx.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoxin
 * @description 交易支付订单实体对象
 * @create 2026/8/26 13:41
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradePaySuccessEntity {

    /** 渠道 */
    private String source;

    /** 来源 */
    private String channel;

    /** 用户ID */
    private String userId;

    /** 外部交易单号 */
    private String outTradeNo;

}
