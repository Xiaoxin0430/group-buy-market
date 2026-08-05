package cn.xx.domain.trade.adapter.repository;

import cn.xx.domain.trade.mode.aggregate.GroupBuyOrderAggregate;
import cn.xx.domain.trade.mode.entity.MarketPayOrderEntity;
import cn.xx.domain.trade.mode.valobj.GroupBuyProgressVO;

/**
 * @author xiaoxin
 * @description 交易仓储服务接口
 * @create 2026/8/5 16:06
 */


public interface ITradeRepository {
    //查询已有的营销订单
    MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(
            String userId,
            String outTradeNo
    );

    //锁定营销订单
    MarketPayOrderEntity lockMarketPayOrder(
            GroupBuyOrderAggregate groupBuyOrderAggregate
    );

    //查询拼团进度
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);
}
