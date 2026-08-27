package cn.xx.domain.trade.service.settlement.filter;

import cn.xx.domain.trade.adapter.repository.ITradeRepository;
import cn.xx.domain.trade.model.entity.GroupBuyTeamEntity;
import cn.xx.domain.trade.model.entity.MarketPayOrderEntity;
import cn.xx.domain.trade.model.entity.TradeSettlementRuleCommandEntity;
import cn.xx.domain.trade.model.entity.TradeSettlementRuleFilterBackEntity;
import cn.xx.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import cn.xx.types.design.framework.link.model2.handler.ILogicHandler;
import cn.xx.types.enums.ResponseCode;
import cn.xx.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author xiaoxin
 * @description 可结算规则过滤；交易时间
 * @create 2026/8/27 16:04
 */

@Slf4j
@Service
public class SettableRuleFilter implements ILogicHandler<
        TradeSettlementRuleCommandEntity,
        TradeSettlementRuleFilterFactory.DynamicContext,
        TradeSettlementRuleFilterBackEntity> {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradeSettlementRuleFilterBackEntity apply(
            TradeSettlementRuleCommandEntity requestParameter,
            TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws Exception {

        log.info(
                "结算规则过滤-有效时间校验{} outTradeNo:{}",
                requestParameter.getUserId(),
                requestParameter.getOutTradeNo()
        );

        // 上下文；获取数据
        MarketPayOrderEntity marketPayOrderEntity =
                dynamicContext.getMarketPayOrderEntity();

        // 查询拼团对象
        GroupBuyTeamEntity groupBuyTeamEntity =
                repository.queryGroupBuyTeamByTeamId(marketPayOrderEntity.getTeamId());

        // 外部交易时间 - 用户支付完成的真实时间
        Date outTradeTime = requestParameter.getOutTradeTime();

        // 外部交易时间必须小于拼团结束时间
        if (!outTradeTime.before(
                groupBuyTeamEntity.getValidEndTime())) {

            log.error(
                    "订单交易时间不在拼团有效时间范围内"
            );

            throw new AppException(
                    ResponseCode.E0106
            );
        }

        // 设置上下文
        dynamicContext.setGroupBuyTeamEntity(groupBuyTeamEntity);

        return next(requestParameter, dynamicContext);
    }
}
