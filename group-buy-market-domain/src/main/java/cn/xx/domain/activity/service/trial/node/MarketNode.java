package cn.xx.domain.activity.service.trial.node;

import cn.xx.domain.activity.model.entity.MarketProductEntity;
import cn.xx.domain.activity.model.entity.TrialBalanceEntity;
import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.xx.domain.activity.model.valobj.SkuVO;
import cn.xx.domain.activity.service.discount.IDiscountCalculateService;
import cn.xx.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import cn.xx.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.xx.domain.activity.service.trial.thread.QueryGroupBuyActivityDiscountVOThreadTask;
import cn.xx.domain.activity.service.trial.thread.QuerySkuVOFromDBThreadTask;
import cn.xx.types.design.framework.tree.StrategyHandler;
import cn.xx.types.enums.ResponseCode;
import cn.xx.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/14 15:16
 */

@Slf4j
@Service
public class MarketNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext , TrialBalanceEntity> {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;     //引入线程池

    @Resource
    private EndNode endNode;

    @Resource   //把四种优惠计算的java bean对象放一块
    private Map<String, IDiscountCalculateService> discountCalculateServiceMap;

    @Override
    protected void multiThread(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        //异步查询活动配置
        QueryGroupBuyActivityDiscountVOThreadTask queryGroupBuyActivityDiscountVOThreadTask = new QueryGroupBuyActivityDiscountVOThreadTask(requestParameter.getSource(), requestParameter.getChannel(), repository);
        FutureTask<GroupBuyActivityDiscountVO> groupBuyActivityDiscountVOFutureTask = new FutureTask<>(queryGroupBuyActivityDiscountVOThreadTask);
        threadPoolExecutor.execute(groupBuyActivityDiscountVOFutureTask);

        //异步查询商品信息
        QuerySkuVOFromDBThreadTask querySkuVOFromDBThreadTask = new QuerySkuVOFromDBThreadTask(requestParameter.getGoodsId(), repository);
        FutureTask<SkuVO> skuVOFutureTask = new FutureTask<>(querySkuVOFromDBThreadTask);
        threadPoolExecutor.execute(skuVOFutureTask);

        //写入上下文
        dynamicContext.setGroupBuyActivityDiscountVO(groupBuyActivityDiscountVOFutureTask.get(timeout, TimeUnit.MINUTES));
        dynamicContext.setSkuVO(skuVOFutureTask.get(timeout, TimeUnit.MINUTES));

        log.info("拼团商品查询试算服务-MarketNode userId:{} 异步线程加载数据「GroupBuyActivityDiscountVO、SkuVO」完成", requestParameter.getUserId());
    }

    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("拼团商品查询试算服务-MarketNode userId:{} requestParameter:{}", requestParameter.getUserId(), JSON.toJSONString(requestParameter));

        // 拼团优惠试算
        //1.从上下文取得活动及配置
        GroupBuyActivityDiscountVO groupBuyActivityDiscountVO = dynamicContext.getGroupBuyActivityDiscountVO();
        GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount = groupBuyActivityDiscountVO.getGroupBuyDiscount();

        //2.获取商品原价
        BigDecimal originalPrice = dynamicContext.getSkuVO().getOriginalPrice();

        //3.获取折扣策略
        IDiscountCalculateService discountCalculateService = discountCalculateServiceMap.get(groupBuyDiscount.getMarketPlan());

        //4.判空
        if (null == discountCalculateService) {
            log.info(
                    "不存在{}类型的折扣计算服务，支持类型为:{}",
                    groupBuyDiscount.getMarketPlan(),
                    JSON.toJSONString(
                            discountCalculateServiceMap.keySet()
                    )
            );

            throw new AppException(
                    ResponseCode.E0001.getCode(),
                    ResponseCode.E0001.getInfo()
            );
        }

        //5.计算
        BigDecimal deductionPrice = discountCalculateService.calculate(requestParameter.getUserId(), originalPrice, groupBuyDiscount);

        //6.写入上下文
        dynamicContext.setDeductionPrice(deductionPrice);

        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        return endNode;
    }
}
