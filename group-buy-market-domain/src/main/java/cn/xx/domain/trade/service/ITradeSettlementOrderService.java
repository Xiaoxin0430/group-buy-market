package cn.xx.domain.trade.service;

import cn.xx.domain.trade.model.entity.TradePaySettlementEntity;
import cn.xx.domain.trade.model.entity.TradePaySuccessEntity;

/**
 * @author xiaoxin
 * @description 拼团交易结算服务接口
 * @create 2026/8/26 13:52
 */


public interface ITradeSettlementOrderService {

    /**
     * 营销结算
     *
     * @param tradePaySuccessEntity 交易支付订单实体对象
     * @return 交易结算订单实体
     */
    TradePaySettlementEntity settlementMarketPayOrder(
            TradePaySuccessEntity tradePaySuccessEntity
    );


}
