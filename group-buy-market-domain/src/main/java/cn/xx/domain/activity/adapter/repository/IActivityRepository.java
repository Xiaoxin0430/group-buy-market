package cn.xx.domain.activity.adapter.repository;

import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.xx.domain.activity.model.valobj.SkuVO;

/**
 * @author xiaoxin
 * @description 活动仓储
 * @create 2026/7/15 14:28
 */


public interface IActivityRepository {
    GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(String source, String channel);

    SkuVO querySkuByGoodsId(String goodsId);
}
