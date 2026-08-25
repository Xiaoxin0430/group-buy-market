package cn.xx.domain.trade.service.filter;

import cn.xx.domain.trade.adapter.repository.ITradeRepository;
import cn.xx.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.xx.domain.trade.model.entity.TradeRuleCommandEntity;
import cn.xx.domain.trade.model.entity.TradeRuleFilterBackEntity;
import cn.xx.domain.trade.service.factory.TradeRuleFilterFactory;
import cn.xx.types.design.framework.link.model2.handler.ILogicHandler;
import cn.xx.types.enums.ResponseCode;
import cn.xx.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaoxin
 * @description 用户参与限制，规则过滤【用户参与次数】
 * @create 2026/8/24 15:18
 */

@Slf4j
@Service
public class UserTakeLimitRuleFilter implements ILogicHandler<
        TradeRuleCommandEntity,
        TradeRuleFilterFactory.DynamicContext,
        TradeRuleFilterBackEntity> {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradeRuleFilterBackEntity apply(
            TradeRuleCommandEntity requestParameter,
            TradeRuleFilterFactory.DynamicContext dynamicContext)
            throws Exception {

        log.info(
                "交易规则过滤-用户参与次数校验{} activityId:{}",
                requestParameter.getUserId(),
                requestParameter.getActivityId()
        );

        GroupBuyActivityEntity groupBuyActivity =
                dynamicContext.getGroupBuyActivity();

        // 查询用户在一个拼团活动上参与的次数
        Integer count =
                repository.queryOrderCountByActivityId(
                        requestParameter.getActivityId(),
                        requestParameter.getUserId()
                );

        if (null != groupBuyActivity.getTakeLimitCount()
                && count >= groupBuyActivity.getTakeLimitCount()) {

            log.info(
                    "用户参与次数校验，已达可参与上限 activityId:{}",
                    requestParameter.getActivityId()
            );

            throw new AppException(ResponseCode.E0103);
        }

        return TradeRuleFilterBackEntity.builder()
                .userTakeOrderCount(count)
                .build();
    }

}
