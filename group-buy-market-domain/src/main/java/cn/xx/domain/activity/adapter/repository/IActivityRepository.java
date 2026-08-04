package cn.xx.domain.activity.adapter.repository;

import cn.xx.domain.activity.model.valobj.SCSkuActivityVO;
import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.xx.domain.activity.model.valobj.SkuVO;

/**
 * @author xiaoxin
 * @description 活动仓储
 * @create 2026/7/15 14:28
 */


public interface IActivityRepository {
    //查询折扣信息
    GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(
            Long activityId
    );

    //查询商品信息
    SkuVO querySkuByGoodsId(String goodsId);

    //查询商品活动关联信息
    SCSkuActivityVO querySCSkuActivityBySCGoodsId(
            String source,  String channel, String goodsId
    );

    //人群判断
    boolean isTagCrowdRange(String tagId, String userId);
}
