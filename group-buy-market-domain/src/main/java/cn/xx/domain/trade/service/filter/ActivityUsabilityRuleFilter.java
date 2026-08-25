package cn.xx.domain.trade.service.filter;

import cn.xx.domain.trade.adapter.repository.ITradeRepository;
import cn.xx.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.xx.domain.trade.model.entity.TradeRuleCommandEntity;
import cn.xx.domain.trade.model.entity.TradeRuleFilterBackEntity;
import cn.xx.domain.trade.service.factory.TradeRuleFilterFactory;
import cn.xx.types.design.framework.link.model2.handler.ILogicHandler;
import cn.xx.types.enums.ActivityStatusEnumVO;
import cn.xx.types.enums.ResponseCode;
import cn.xx.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author xiaoxin
 * @description 判断活动的可用性，规则过滤【状态、有效期】
 * @create 2026/8/24 15:01
 */

@Slf4j
@Service
public class ActivityUsabilityRuleFilter implements ILogicHandler<
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

        //查询活动
        GroupBuyActivityEntity groupBuyActivity =
                repository.queryGroupBuyActivityEntityByActivityId(
                                requestParameter.getActivityId()
                        );

        //判断活动状态
        if (!ActivityStatusEnumVO.EFFECTIVE.equals(groupBuyActivity.getStatus())) {

            log.info(
                    "活动的可用性校验，非生效状态 activityId:{}",
                    requestParameter.getActivityId()
            );

            throw new AppException(ResponseCode.E0101);
        }

        Date currentTime = new Date();
        if (currentTime.before(groupBuyActivity.getStartTime())
                ||
                currentTime.after(groupBuyActivity.getEndTime())) {
            throw new AppException(ResponseCode.E0102);
        }

        dynamicContext.setGroupBuyActivity(groupBuyActivity);

        return next(requestParameter, dynamicContext);

    }
}
