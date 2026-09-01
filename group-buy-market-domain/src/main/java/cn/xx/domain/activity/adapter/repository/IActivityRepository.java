package cn.xx.domain.activity.adapter.repository;

import cn.xx.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import cn.xx.domain.activity.model.valobj.SCSkuActivityVO;
import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.xx.domain.activity.model.valobj.SkuVO;
import cn.xx.domain.activity.model.valobj.TeamStatisticVO;

import java.util.List;

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

    /**
     * 判断是否开启拼团活动降级。
     *
     * true：开启降级，需要拦截请求
     * false：未开启降级，可以继续处理
     */
    boolean downgradeSwitch();
    /**
     * 判断当前用户是否在灰度放量范围内。
     *
     * true：允许用户进入拼团流程
     * false：用户不在放量范围
     */
    boolean cutRange(String userId);

    List<UserGroupBuyOrderDetailEntity> queryInProgressUserGroupBuyOrderDetailListByOwner(
            Long activityId, String userId, Integer ownerCount);


    List<UserGroupBuyOrderDetailEntity>
    queryInProgressUserGroupBuyOrderDetailListByRandom(
            Long activityId, String userId, Integer randomCount);

    TeamStatisticVO queryTeamStatisticByActivityId(Long activityId);
}
