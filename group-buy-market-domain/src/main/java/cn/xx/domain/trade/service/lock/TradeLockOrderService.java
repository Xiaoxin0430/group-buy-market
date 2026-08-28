package cn.xx.domain.trade.service.lock;

import cn.xx.domain.trade.adapter.repository.ITradeRepository;
import cn.xx.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import cn.xx.domain.trade.model.entity.*;
import cn.xx.domain.trade.model.valobj.GroupBuyProgressVO;
import cn.xx.domain.trade.service.ITradeLockOrderService;
import cn.xx.domain.trade.service.lock.factory.TradeLockRuleFilterFactory;
import cn.xx.types.design.framework.link.model2.chain.BusinessLinkedList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaoxin
 * @description 交易订单服务
 * @create 2026/8/26 13:32
 */

@Slf4j
@Service
public class TradeLockOrderService implements ITradeLockOrderService {

    @Resource
    private ITradeRepository repository;

    @Resource
    private BusinessLinkedList<
            TradeLockRuleCommandEntity,
            TradeLockRuleFilterFactory.DynamicContext,
            TradeLockRuleFilterBackEntity> tradeRuleFilter;


    @Override
    public MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo) {
        log.info(
                "拼团交易-查询未支付营销订单:{} outTradeNo:{}",
                userId,
                outTradeNo);

        return repository.queryMarketPayOrderEntityByOutTradeNo(
                userId,
                outTradeNo);
    }

    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {
        log.info("拼团交易-查询拼单进度:{}", teamId);
        return repository.queryGroupBuyProgress(teamId);
    }

    @Override
    public MarketPayOrderEntity lockMarketPayOrder(
            UserEntity userEntity,
            PayActivityEntity payActivityEntity,
            PayDiscountEntity payDiscountEntity) throws Exception {

        log.info(
                "拼团交易-锁定营销优惠支付订单:{} activityId:{} goodsId:{}",
                userEntity.getUserId(),
                payActivityEntity.getActivityId(),
                payDiscountEntity.getGoodsId());

        // 交易规则过滤
        TradeLockRuleFilterBackEntity tradeRuleFilterBackEntity =
                tradeRuleFilter.apply(
                        TradeLockRuleCommandEntity.builder()
                                .activityId(payActivityEntity.getActivityId())
                                .userId(userEntity.getUserId())
                                .build(),
                        new TradeLockRuleFilterFactory.DynamicContext());

        // 已参与拼团量
        Integer userTakeOrderCount =
                tradeRuleFilterBackEntity.getUserTakeOrderCount();

        // 构建聚合对象
        GroupBuyOrderAggregate groupBuyOrderAggregate =
                GroupBuyOrderAggregate.builder()
                        .userEntity(userEntity)
                        .payActivityEntity(payActivityEntity)
                        .payDiscountEntity(payDiscountEntity)
                        .userTakeOrderCount(userTakeOrderCount)
                        .build();

        // 用户当前只是锁单，还没有支付
        return repository.lockMarketPayOrder(groupBuyOrderAggregate);
    }

}
