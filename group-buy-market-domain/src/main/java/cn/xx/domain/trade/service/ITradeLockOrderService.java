package cn.xx.domain.trade.service;

import cn.xx.domain.trade.model.entity.MarketPayOrderEntity;
import cn.xx.domain.trade.model.entity.PayActivityEntity;
import cn.xx.domain.trade.model.entity.PayDiscountEntity;
import cn.xx.domain.trade.model.entity.UserEntity;
import cn.xx.domain.trade.model.valobj.GroupBuyProgressVO;

/**
 * @author xiaoxin
 * @description 拼团交易锁单服务接口
 * @create 2026/8/26 13:31
 */


public interface ITradeLockOrderService {

    /**
     * 查询，未被支付消费完成的营销优惠订单
     *
     * @param userId     用户ID
     * @param outTradeNo 外部唯一单号
     * @return 拼团，预购订单营销实体对象
     */
    MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(
            String userId,
            String outTradeNo);

    /**
     * 查询拼团进度
     *
     * @param teamId 拼团ID
     * @return 进度
     */
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    /**
     * 锁定，营销预支付订单；商品下单前，预购锁定。
     */
    MarketPayOrderEntity lockMarketPayOrder(
            UserEntity userEntity,
            PayActivityEntity payActivityEntity,
            PayDiscountEntity payDiscountEntity) throws Exception;

}
