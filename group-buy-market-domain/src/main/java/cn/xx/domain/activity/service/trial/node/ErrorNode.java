package cn.xx.domain.activity.service.trial.node;

import cn.xx.domain.activity.model.entity.MarketProductEntity;
import cn.xx.domain.activity.model.entity.TrialBalanceEntity;
import cn.xx.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import cn.xx.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.xx.types.design.framework.tree.StrategyHandler;
import cn.xx.types.enums.ResponseCode;
import cn.xx.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/3 20:13
 */


@Slf4j
@Service
public class ErrorNode extends AbstractGroupBuyMarketSupport<
        MarketProductEntity,
        DefaultActivityStrategyFactory.DynamicContext,
        TrialBalanceEntity> {

    @Override
    protected TrialBalanceEntity doApply(
            MarketProductEntity requestParameter,
            DefaultActivityStrategyFactory.DynamicContext dynamicContext)
            throws Exception {

        log.info(
                "拼团商品查询试算服务-NoMarketNode userId:{} requestParameter:{}",
                requestParameter.getUserId(),
                JSON.toJSONString(requestParameter)
        );

        // 无营销配置
        if (null == dynamicContext.getGroupBuyActivityDiscountVO()
                || null == dynamicContext.getSkuVO()) {

            log.info(
                    "商品无拼团营销配置 {}",
                    requestParameter.getGoodsId()
            );

            throw new AppException(
                    ResponseCode.E0002.getCode(),
                    ResponseCode.E0002.getInfo()
            );
        }

        return TrialBalanceEntity.builder().build();
    }

    @Override
    public StrategyHandler<
                MarketProductEntity,
                DefaultActivityStrategyFactory.DynamicContext,
                TrialBalanceEntity> get(
            MarketProductEntity requestParameter,
            DefaultActivityStrategyFactory.DynamicContext dynamicContext)
            throws Exception {

        return defaultStrategyHandler;
    }
}