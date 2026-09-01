package cn.xx.domain.activity.service;

import cn.xx.domain.activity.model.entity.MarketProductEntity;
import cn.xx.domain.activity.model.entity.TrialBalanceEntity;
import cn.xx.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import cn.xx.domain.activity.model.valobj.TeamStatisticVO;

import java.util.List;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/14 15:22
 */


public interface IIndexGroupBuyMarketService {

    /**
     * 营销试算
     */
    TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception;

    /**
     * 查询进行中的拼团订单
     *
     * @param activityId  活动ID
     * @param userId      用户ID
     * @param ownerCount  个人数量
     * @param randomCount 随机数量
     * @return 用户拼团明细数据
     */
    List<UserGroupBuyOrderDetailEntity> queryInProgressUserGroupBuyOrderDetailList(
            Long activityId, String userId, Integer ownerCount, Integer randomCount);

    /**
     * 活动拼团队伍总结
     *
     * @param activityId 活动ID
     * @return 队伍统计
     */
    TeamStatisticVO queryTeamStatisticByActivityId(Long activityId);

}
