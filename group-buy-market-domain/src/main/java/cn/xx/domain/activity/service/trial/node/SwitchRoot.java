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

import javax.annotation.Resource;

/**
 * @author xiaoxin
 * @description 开关节点
 * @create 2026/7/14 15:08
 */

@Slf4j
@Service
public class SwitchRoot extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext , TrialBalanceEntity> {

    @Resource
    private MarketNode marketNode;

    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {

        log.info("拼团商品查询试算服务-SwitchNode userId:{} requestParameter:{}", requestParameter.getUserId(), JSON.toJSONString(requestParameter));

        String userId = requestParameter.getUserId();

        if (repository.downgradeSwitch()) {
            log.info("拼团活动降级拦截 {}", userId);

            throw new AppException(
                    ResponseCode.E0003.getCode(),
                    ResponseCode.E0003.getInfo()
            );
        }

        if (!repository.cutRange(userId)) {
            log.info("拼团活动切量拦截 {}", userId);

            throw new AppException(
                    ResponseCode.E0004.getCode(),
                    ResponseCode.E0004.getInfo()
            );
        }


        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        return marketNode;
    }
}
