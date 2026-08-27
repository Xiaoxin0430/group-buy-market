package cn.xx.domain.trade.service.settlement.filter;

import cn.xx.domain.trade.adapter.repository.ITradeRepository;
import cn.xx.domain.trade.model.entity.TradeSettlementRuleCommandEntity;
import cn.xx.domain.trade.model.entity.TradeSettlementRuleFilterBackEntity;
import cn.xx.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import cn.xx.types.design.framework.link.model2.handler.ILogicHandler;
import cn.xx.types.enums.ResponseCode;
import cn.xx.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaoxin
 * @description SC 渠道来源过滤
 * @create 2026/8/27 15:56
 */

@Slf4j
@Service
public class SCRuleFilter implements ILogicHandler<
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
                "结算规则过滤-渠道黑名单校验{} outTradeNo:{}",
                requestParameter.getUserId(),
                requestParameter.getOutTradeNo()
        );

        // sc 渠道黑名单拦截
        boolean intercept = repository.isSCBlackIntercept(
                requestParameter.getSource(),
                requestParameter.getChannel()
        );

        if (intercept) {
            log.error(
                    "{}{} 渠道黑名单拦截",
                    requestParameter.getSource(),
                    requestParameter.getChannel()
            );

            throw new AppException(ResponseCode.E0105);
        }

        return next(requestParameter, dynamicContext);
    }
}
