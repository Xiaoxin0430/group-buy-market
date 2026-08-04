package cn.xx.domain.activity.service.trial.thread;

import cn.xx.domain.activity.adapter.repository.IActivityRepository;
import cn.xx.domain.activity.model.valobj.SCSkuActivityVO;
import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.util.concurrent.Callable;

/**
 * @author xiaoxin
 * @description在异步活动查询任务中组织拆表后的两阶段查询，先取得商品关联的 activityId，再加载完整活动及折扣配置，同时保持整条活动查询链与商品查询并行执行。
 * @create 2026/7/14 20:31
 */


public class QueryGroupBuyActivityDiscountVOThreadTask implements Callable<GroupBuyActivityDiscountVO> {

    /**
     * 来源
     */
    private final String source;

    /**
     * 渠道
     */
    private final String channel;

    /**
     * 商品id
     */
    private final String goodsId;

    /**
     * 活动仓储
     */
    private final IActivityRepository activityRepository;

    public QueryGroupBuyActivityDiscountVOThreadTask(String source, String channel, String goodsId, IActivityRepository activityRepository) {
        this.source = source;
        this.channel = channel;
        this.goodsId = goodsId;
        this.activityRepository = activityRepository;
    }

    @Override
    public GroupBuyActivityDiscountVO call() throws Exception {
        // 查询渠道、商品与活动的关联配置
        SCSkuActivityVO scSkuActivityVO = activityRepository.querySCSkuActivityBySCGoodsId(source, channel, goodsId);

        if (null == scSkuActivityVO) return null;

        // 根据关联关系中的活动ID查询活动配置
        return activityRepository.queryGroupBuyActivityDiscountVO(
                scSkuActivityVO.getActivityId()
        );
    }
}
