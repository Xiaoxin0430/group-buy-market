package cn.xx.infrastructure.adapter.repository;

import cn.xx.domain.activity.adapter.repository.IActivityRepository;
import cn.xx.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import cn.xx.domain.activity.model.valobj.*;
import cn.xx.infrastructure.dao.*;
import cn.xx.infrastructure.dao.po.*;
import cn.xx.infrastructure.dcc.DCCService;
import cn.xx.infrastructure.redis.IRedisService;
import org.redisson.api.RBitSet;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/15 14:37
 */

@Repository
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;

    @Resource
    private IGroupBuyDiscountDao groupBuyDiscountDao;

    @Resource
    private ISkuDao skuDao;

    @Resource
    private ISCSkuActivityDao scSkuActivityDao;

    @Resource
    private IGroupBuyOrderDao groupBuyOrderDao;

    @Resource
    private IGroupBuyOrderListDao groupBuyOrderListDao;

    @Resource
    private IRedisService redisService;

    @Resource
    private DCCService dccService;

    //查询优惠活动信息，装配为vo对象
    @Override
    public GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(Long activityId) {

        GroupBuyActivity groupBuyActivityRes = groupBuyActivityDao.queryValidGroupBuyActivityById(activityId);
        if (null == groupBuyActivityRes) return null;

        String discountId = groupBuyActivityRes.getDiscountId();
        GroupBuyDiscount groupBuyDiscountRes = groupBuyDiscountDao.queryGroupBuyActivityDiscountByDiscountId(discountId);
        if (null == groupBuyActivityRes) return null;

        GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount = GroupBuyActivityDiscountVO.GroupBuyDiscount.builder()
                .discountName(groupBuyDiscountRes.getDiscountName())
                .discountDesc(groupBuyDiscountRes.getDiscountDesc())
                .discountType(DiscountTypeEnum.get(groupBuyDiscountRes.getDiscountType()))
                .marketPlan(groupBuyDiscountRes.getMarketPlan())
                .marketExpr(groupBuyDiscountRes.getMarketExpr())
                .tagId(groupBuyDiscountRes.getTagId())
                .build();

        return GroupBuyActivityDiscountVO.builder()
                .activityId(groupBuyActivityRes.getActivityId())
                .activityName(groupBuyActivityRes.getActivityName())
                .groupBuyDiscount(groupBuyDiscount)
                .groupType(groupBuyActivityRes.getGroupType())
                .takeLimitCount(groupBuyActivityRes.getTakeLimitCount())
                .target(groupBuyActivityRes.getTarget())
                .validTime(groupBuyActivityRes.getValidTime())
                .status(groupBuyActivityRes.getStatus())
                .startTime(groupBuyActivityRes.getStartTime())
                .endTime(groupBuyActivityRes.getEndTime())
                .tagId(groupBuyActivityRes.getTagId())
                .tagScope(groupBuyActivityRes.getTagScope())
                .build();
    }

    @Override
    public SkuVO querySkuByGoodsId(String goodsId) {
        Sku sku = skuDao.querySkuByGoodsId(goodsId);
        return SkuVO.builder()
                .goodsId(sku.getGoodsId())
                .goodsName(sku.getGoodsName())
                .originalPrice(sku.getOriginalPrice())
                .build();
    }

    //查询商品与活动的关联信息
    @Override
    public SCSkuActivityVO querySCSkuActivityBySCGoodsId(String source, String channel, String goodsId) {
        SCSkuActivity scSkuActivityReq = new SCSkuActivity();
        scSkuActivityReq.setSource(source);
        scSkuActivityReq.setChannel(channel);
        scSkuActivityReq.setGoodsId(goodsId);

        SCSkuActivity scSkuActivity = scSkuActivityDao.querySCSkuActivityBySCGoodsId(scSkuActivityReq);
        if (null == scSkuActivity) return null;

        return SCSkuActivityVO.builder()
                .source(scSkuActivity.getSource())
                .channel(scSkuActivity.getChannel())
                .activityId(scSkuActivity.getActivityId())
                .goodsId(scSkuActivity.getGoodsId())
                .build();
    }

    //人群判断
    @Override
    public boolean isTagCrowdRange(String tagId, String userId) {
        RBitSet bitSet = redisService.getBitSet(tagId);
        //不存在位图中默认放行，存在就取实际的值
        if (!bitSet.isExists()){
            return true;
        }

        return bitSet.get(redisService.getIndexFromUserId(userId));
    }

    @Override
    public boolean downgradeSwitch() {
        return dccService.isDowngradeSwitch();
    }

    @Override
    public boolean cutRange(String userId) {
        return dccService.isCutRange(userId);
    }

    //查询当前用户的置顶队伍
    @Override
    public List<UserGroupBuyOrderDetailEntity> queryInProgressUserGroupBuyOrderDetailListByOwner(Long activityId, String userId, Integer ownerCount) {

        // 1. 查当前用户在本活动下的进行中订单明细。
        GroupBuyOrderList query = new GroupBuyOrderList();
        query.setActivityId(activityId);
        query.setUserId(userId);
        query.setCount(ownerCount);

        List<GroupBuyOrderList> orderLists =
                groupBuyOrderListDao
                        .queryInProgressUserGroupBuyOrderDetailListByUserId(query);

        if (null == orderLists || orderLists.isEmpty()) {
            return null;
        }

        // 2. 由订单明细提取 teamId，批量查询队伍主单，避免逐条查库。
        Set<String> teamIds = orderLists.stream()
                .map(GroupBuyOrderList::getTeamId)
                .filter(teamId -> teamId != null && !teamId.isEmpty())
                .collect(Collectors.toSet());

        List<GroupBuyOrder> orders =
                groupBuyOrderDao.queryGroupBuyProgressByTeamIds(teamIds);

        if (null == orders || orders.isEmpty()) {
            return null;
        }

        Map<String, GroupBuyOrder> orderMap = orders.stream()
                .collect(Collectors.toMap(GroupBuyOrder::getTeamId, order -> order));

        // 3. 合并“用户订单明细”和“队伍主单”。
        List<UserGroupBuyOrderDetailEntity> result = new ArrayList<>();
        for (GroupBuyOrderList orderList : orderLists) {
            GroupBuyOrder order = orderMap.get(orderList.getTeamId());
            if (null == order) {
                continue;
            }

            result.add(UserGroupBuyOrderDetailEntity.builder()
                    .userId(orderList.getUserId())
                    .teamId(order.getTeamId())
                    .activityId(order.getActivityId())
                    .targetCount(order.getTargetCount())
                    .completeCount(order.getCompleteCount())
                    .lockCount(order.getLockCount())
                    .validStartTime(order.getValidStartTime())
                    .validEndTime(order.getValidEndTime())
                    .outTradeNo(orderList.getOutTradeNo())
                    .build());
        }

        return result;
    }

    @Override
    public List<UserGroupBuyOrderDetailEntity> queryInProgressUserGroupBuyOrderDetailListByRandom(Long activityId, String userId, Integer randomCount) {
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setActivityId(activityId);
        groupBuyOrderListReq.setUserId(userId);
        groupBuyOrderListReq.setCount(randomCount * 2);
        List<GroupBuyOrderList> groupBuyOrderLists = groupBuyOrderListDao.queryInProgressUserGroupBuyOrderDetailListByRandom(groupBuyOrderListReq);
        if (null == groupBuyOrderLists || groupBuyOrderLists.isEmpty()) return null;

        if (groupBuyOrderLists.size() > randomCount) {
            Collections.shuffle(groupBuyOrderLists);
            groupBuyOrderLists = groupBuyOrderLists.subList(0, randomCount);
        }

        Set<String> teamIds = groupBuyOrderLists.stream()
                .map(GroupBuyOrderList::getTeamId)
                .filter(teamId -> teamId != null && !teamId.isEmpty())
                .collect(Collectors.toSet());

        List<GroupBuyOrder> groupBuyOrders = groupBuyOrderDao.queryGroupBuyProgressByTeamIds(teamIds);
        if (null == groupBuyOrders || groupBuyOrders.isEmpty()) return null;

        Map<String, GroupBuyOrder> groupBuyOrderMap = groupBuyOrders.stream()
                .collect(Collectors.toMap(GroupBuyOrder::getTeamId, order -> order));

        List<UserGroupBuyOrderDetailEntity> userGroupBuyOrderDetailEntities = new ArrayList<>();
        for (GroupBuyOrderList groupBuyOrderList : groupBuyOrderLists) {
            GroupBuyOrder groupBuyOrder = groupBuyOrderMap.get(groupBuyOrderList.getTeamId());
            if (null == groupBuyOrder) continue;

            userGroupBuyOrderDetailEntities.add(UserGroupBuyOrderDetailEntity.builder()
                    .userId(groupBuyOrderList.getUserId())
                    .teamId(groupBuyOrder.getTeamId())
                    .activityId(groupBuyOrder.getActivityId())
                    .targetCount(groupBuyOrder.getTargetCount())
                    .completeCount(groupBuyOrder.getCompleteCount())
                    .lockCount(groupBuyOrder.getLockCount())
                    .validStartTime(groupBuyOrder.getValidStartTime())
                    .validEndTime(groupBuyOrder.getValidEndTime())
                    .build());
        }

        return userGroupBuyOrderDetailEntities;
    }

    @Override
    public TeamStatisticVO queryTeamStatisticByActivityId(Long activityId) {
        List<GroupBuyOrderList> groupBuyOrderLists = groupBuyOrderListDao.queryInProgressUserGroupBuyOrderDetailListByActivityId(activityId);
        if (null == groupBuyOrderLists || groupBuyOrderLists.isEmpty()) {
            return new TeamStatisticVO(0, 0, 0);
        }

        Set<String> teamIds = groupBuyOrderLists.stream()
                .map(GroupBuyOrderList::getTeamId)
                .filter(teamId -> teamId != null && !teamId.isEmpty())
                .collect(Collectors.toSet());

        Integer allTeamCount = groupBuyOrderDao.queryAllTeamCount(teamIds);
        Integer allTeamCompleteCount = groupBuyOrderDao.queryAllTeamCompleteCount(teamIds);
        Integer allTeamUserCount = groupBuyOrderDao.queryAllUserCount(teamIds);

        return TeamStatisticVO.builder()
                .allTeamCount(allTeamCount)
                .allTeamCompleteCount(allTeamCompleteCount)
                .allTeamUserCount(allTeamUserCount)
                .build();
    }
}
