package cn.xx.domain.trade.service;

import cn.xx.domain.trade.adapter.repository.ITradeRepository;
import cn.xx.domain.trade.mode.aggregate.GroupBuyOrderAggregate;
import cn.xx.domain.trade.mode.entity.MarketPayOrderEntity;
import cn.xx.domain.trade.mode.entity.PayActivityEntity;
import cn.xx.domain.trade.mode.entity.PayDiscountEntity;
import cn.xx.domain.trade.mode.entity.UserEntity;
import cn.xx.domain.trade.mode.valobj.GroupBuyProgressVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaoxin
 * @description 交易订单服务
 * @create 2026/8/5 16:32
 */

@Slf4j
@Service
public class TradeOrderService implements ITradeOrderService{

    @Resource
    private ITradeRepository repository;

    @Override
    public MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo) {

        log.info("拼团交易-查询未支付营销订单:{} outTradeNo:{}", userId, outTradeNo);

        return repository.queryMarketPayOrderEntityByOutTradeNo(
                userId,
                outTradeNo
        );

    }

    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {

        log.info("拼团交易-查询拼单进度:{}", teamId);

        return repository.queryGroupBuyProgress(teamId);
    }

    //核心功能锁单
    @Override
    public MarketPayOrderEntity lockMarketPayOrder(
            UserEntity userEntity,
            PayActivityEntity payActivityEntity,
            PayDiscountEntity payDiscountEntity)
    {

        log.info(
                "拼团交易-锁定营销优惠支付订单:{} activityId:{} goodsId:{}",
                userEntity.getUserId(),
                payActivityEntity.getActivityId(),
                payDiscountEntity.getGoodsId()
        );

        // 构建聚合对象
        GroupBuyOrderAggregate groupBuyOrderAggregate =
                GroupBuyOrderAggregate.builder()
                        .userEntity(userEntity)
                        .payActivityEntity(payActivityEntity)
                        .payDiscountEntity(payDiscountEntity)
                        .build();

        // 锁定聚合订单
        // 这时用户只是下单，还没有支付。
        // 后续有两个流程：支付成功、超时未支付回退。
        return repository.lockMarketPayOrder(
                groupBuyOrderAggregate
        );
    }
}
