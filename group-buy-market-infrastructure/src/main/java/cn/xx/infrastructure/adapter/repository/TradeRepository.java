package cn.xx.infrastructure.adapter.repository;

import cn.xx.domain.trade.adapter.repository.ITradeRepository;
import cn.xx.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import cn.xx.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import cn.xx.domain.trade.model.entity.*;
import cn.xx.domain.trade.model.valobj.GroupBuyProgressVO;
import cn.xx.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import cn.xx.infrastructure.dao.IGroupBuyActivityDao;
import cn.xx.infrastructure.dao.IGroupBuyOrderDao;
import cn.xx.infrastructure.dao.IGroupBuyOrderListDao;
import cn.xx.infrastructure.dao.INotifyTaskDao;
import cn.xx.infrastructure.dao.po.GroupBuyActivity;
import cn.xx.infrastructure.dao.po.GroupBuyOrder;
import cn.xx.infrastructure.dao.po.GroupBuyOrderList;
import cn.xx.infrastructure.dao.po.NotifyTask;
import cn.xx.infrastructure.dcc.DCCService;
import cn.xx.types.common.Constants;
import cn.xx.types.enums.ActivityStatusEnumVO;
import cn.xx.types.enums.GroupBuyOrderEnumVO;
import cn.xx.types.enums.ResponseCode;
import cn.xx.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author xiaoxin
 * @description 交易仓储服务
 * @create 2026/8/5 16:11
 */

@Slf4j
@Repository
public class TradeRepository implements ITradeRepository {

    @Resource
    private IGroupBuyOrderDao groupBuyOrderDao;

    @Resource
    private IGroupBuyOrderListDao groupBuyOrderListDao;

    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;

    @Resource
    private INotifyTaskDao notifyTaskDao;

    @Resource
    private DCCService dccService;

    @Override
    public MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo) {
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();

        groupBuyOrderListReq.setUserId(userId);
        groupBuyOrderListReq.setOutTradeNo(outTradeNo);

        GroupBuyOrderList groupBuyOrderListRes =
                groupBuyOrderListDao.queryGroupBuyOrderRecordByOutTradeNo(groupBuyOrderListReq);

        if (null == groupBuyOrderListRes) {
            return null;
        }

        return MarketPayOrderEntity.builder()
                .teamId(groupBuyOrderListRes.getTeamId())
                .orderId(groupBuyOrderListRes.getOrderId())
                .deductionPrice(groupBuyOrderListRes.getDeductionPrice())
                .tradeOrderStatusEnumVO(
                        TradeOrderStatusEnumVO.valueOf(
                                groupBuyOrderListRes.getStatus()
                        )
                )
                .build();
    }

    //核心功能锁单
    @Transactional(timeout = 500)
    @Override
    public MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate) {
        //拆分聚合对象
        UserEntity userEntity =
                groupBuyOrderAggregate.getUserEntity();
        PayActivityEntity payActivityEntity =
                groupBuyOrderAggregate.getPayActivityEntity();
        PayDiscountEntity payDiscountEntity =
                groupBuyOrderAggregate.getPayDiscountEntity();
        Integer userTakeOrderCount =
                groupBuyOrderAggregate.getUserTakeOrderCount();

        //判断新团还是已有团
        String teamId = payActivityEntity.getTeamId();
        if (StringUtils.isBlank(teamId)) {
            // 开新团
            teamId = RandomStringUtils.randomNumeric(8);

            // 日期处理
            Date currentDate = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            calendar.add(
                    Calendar.MINUTE,
                    payActivityEntity.getValidTime()
            );

            //创建新团po对象
            GroupBuyOrder groupBuyOrder =
                    GroupBuyOrder.builder()
                            .teamId(teamId)
                            .activityId(
                                    payActivityEntity.getActivityId()
                            )
                            .source(
                                    payDiscountEntity.getSource()
                            )
                            .channel(
                                    payDiscountEntity.getChannel()
                            )
                            .originalPrice(
                                    payDiscountEntity.getOriginalPrice()
                            )
                            .deductionPrice(
                                    payDiscountEntity.getDeductionPrice()
                            )
                            .payPrice(
                                    payDiscountEntity.getPayPrice()
                            )
                            .targetCount(
                                    payActivityEntity.getTargetCount()
                            )
                            .completeCount(0)
                            .lockCount(1)
                            .validStartTime(currentDate)
                            .validEndTime(calendar.getTime())
                            .build();

            groupBuyOrderDao.insert(groupBuyOrder);
        } else {
            // 加入已有团
            int updateAddTargetCount = groupBuyOrderDao.updateAddLockCount(teamId);

            // 更新不到一条记录，说明不能加入
            if (1 != updateAddTargetCount) {
                throw new AppException(ResponseCode.E0005);
            }
        }

        //生成个人营销订单号
        String orderId = RandomStringUtils.randomNumeric(12);
        //构建用户订单明细
        GroupBuyOrderList groupBuyOrderListReq =
                GroupBuyOrderList.builder()
                        .userId(userEntity.getUserId())
                        .teamId(teamId)
                        .orderId(orderId)
                        .activityId(
                                payActivityEntity.getActivityId()
                        )
                        .startTime(
                                payActivityEntity.getStartTime()
                        )
                        .endTime(
                                payActivityEntity.getEndTime()
                        )
                        .goodsId(
                                payDiscountEntity.getGoodsId()
                        )
                        .source(
                                payDiscountEntity.getSource()
                        )
                        .channel(
                                payDiscountEntity.getChannel()
                        )
                        .originalPrice(
                                payDiscountEntity.getOriginalPrice()
                        )
                        .deductionPrice(
                                payDiscountEntity.getDeductionPrice()
                        )
                        .status(
                                TradeOrderStatusEnumVO.CREATE.getCode()
                        )
                        .outTradeNo(
                                payDiscountEntity.getOutTradeNo()
                        )
                        //活动id 用户id 参与次数
                        .bizId(
                                payActivityEntity.getActivityId()
                                        + Constants.UNDERLINE
                                        + userEntity.getUserId()
                                        + Constants.UNDERLINE
                                        + (userTakeOrderCount + 1)
                        )
                        .build();

        try {
            //写入用户订单明细
            groupBuyOrderListDao.insert(groupBuyOrderListReq);
        } catch (DuplicateKeyException e) {
            throw new AppException(
                    ResponseCode.INDEX_EXCEPTION
            );
        }

        return MarketPayOrderEntity.builder()
                .orderId(orderId)
                .deductionPrice(
                        payDiscountEntity.getDeductionPrice()
                )
                .tradeOrderStatusEnumVO(
                        TradeOrderStatusEnumVO.CREATE
                )
                .build();
    }

    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {
        GroupBuyOrder groupBuyOrder = groupBuyOrderDao.queryGroupBuyProgress(teamId);

        if (null == groupBuyOrder) {
            return null;
        }

        return GroupBuyProgressVO.builder()
                .completeCount(
                        groupBuyOrder.getCompleteCount()
                )
                .targetCount(
                        groupBuyOrder.getTargetCount()
                )
                .lockCount(
                        groupBuyOrder.getLockCount()
                )
                .build();
    }

    //查活动信息
    @Override
    public GroupBuyActivityEntity queryGroupBuyActivityEntityByActivityId(Long activityId) {
        GroupBuyActivity groupBuyActivity =
                groupBuyActivityDao
                        .queryGroupBuyActivityByActivityId(activityId);


        return GroupBuyActivityEntity.builder()
                .activityId(groupBuyActivity.getActivityId())
                .activityName(groupBuyActivity.getActivityName())
                .discountId(groupBuyActivity.getDiscountId())
                .groupType(groupBuyActivity.getGroupType())
                .takeLimitCount(groupBuyActivity.getTakeLimitCount())
                .target(groupBuyActivity.getTarget())
                .validTime(groupBuyActivity.getValidTime())
                .status(
                        ActivityStatusEnumVO.valueOf(
                                groupBuyActivity.getStatus()
                        )
                )
                .startTime(groupBuyActivity.getStartTime())
                .endTime(groupBuyActivity.getEndTime())
                .tagId(groupBuyActivity.getTagId())
                .tagScope(groupBuyActivity.getTagScope())
                .build();

    }

    //查询用户参加活动次数
    @Override
    public Integer queryOrderCountByActivityId(Long activityId, String userId) {
        GroupBuyOrderList groupBuyOrderListReq =
                new GroupBuyOrderList();

        groupBuyOrderListReq.setActivityId(activityId);
        groupBuyOrderListReq.setUserId(userId);

        return groupBuyOrderListDao
                .queryOrderCountByActivityId(groupBuyOrderListReq);

    }

    @Override
    public GroupBuyTeamEntity queryGroupBuyTeamByTeamId(String teamId) {
        GroupBuyOrder groupBuyOrder =
                groupBuyOrderDao.queryGroupBuyTeamByTeamId(teamId);

        return GroupBuyTeamEntity.builder()
                .teamId(groupBuyOrder.getTeamId())
                .activityId(groupBuyOrder.getActivityId())
                .targetCount(groupBuyOrder.getTargetCount())
                .completeCount(groupBuyOrder.getCompleteCount())
                .lockCount(groupBuyOrder.getLockCount())
                .status(
                        GroupBuyOrderEnumVO.valueOf(
                                groupBuyOrder.getStatus()))
                .validStartTime(groupBuyOrder.getValidStartTime())
                .validEndTime(groupBuyOrder.getValidEndTime())
                .build();
    }

    @Transactional(timeout = 500)
    @Override
    public void settlementMarketPayOrder(
            GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate) {

        //拆分聚合对象
        UserEntity userEntity =
                groupBuyTeamSettlementAggregate.getUserEntity();

        GroupBuyTeamEntity groupBuyTeamEntity =
                groupBuyTeamSettlementAggregate.getGroupBuyTeamEntity();

        TradePaySuccessEntity tradePaySuccessEntity =
                groupBuyTeamSettlementAggregate.getTradePaySuccessEntity();


        // 1. 更新拼团订单明细状态
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();

        groupBuyOrderListReq.setUserId(userEntity.getUserId());

        groupBuyOrderListReq.setOutTradeNo(tradePaySuccessEntity.getOutTradeNo());

        groupBuyOrderListReq.setOutTradeTime(tradePaySuccessEntity.getOutTradeTime());

        int updateOrderListStatusCount =
                groupBuyOrderListDao.updateOrderStatus2COMPLETE(groupBuyOrderListReq);

        if (1 != updateOrderListStatusCount) {
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }

        // 2. 更新拼团达成数量
        int updateAddCount =
                groupBuyOrderDao.updateAddCompleteCount(groupBuyTeamEntity.getTeamId());

        if (1 != updateAddCount) {
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }

        // 3. 更新拼团完成状态
        if (groupBuyTeamEntity.getTargetCount() - groupBuyTeamEntity.getCompleteCount() == 1) {

            int updateOrderStatusCount =
                    groupBuyOrderDao.updateOrderStatus2COMPLETE(groupBuyTeamEntity.getTeamId());

            if (1 != updateOrderStatusCount) {
                throw new AppException(ResponseCode.UPDATE_ZERO);
            }

            // 查询拼团交易完成外部单号列表
            List<String> outTradeNoList =
                    groupBuyOrderListDao.queryGroupBuyCompleteOrderOutTradeNoListByTeamId(
                                    groupBuyTeamEntity.getTeamId());

            // 拼团完成写入回调任务记录
            NotifyTask notifyTask = new NotifyTask();

            notifyTask.setActivityId(groupBuyTeamEntity.getActivityId());

            notifyTask.setTeamId(groupBuyTeamEntity.getTeamId());

            notifyTask.setNotifyUrl("暂无");
            notifyTask.setNotifyCount(0);
            notifyTask.setNotifyStatus(0);

            notifyTask.setParameterJson(
                    JSON.toJSONString(
                            new HashMap<String, Object>() {{
                                put(
                                        "teamId",
                                        groupBuyTeamEntity
                                                .getTeamId());

                                put(
                                        "outTradeNoList",
                                        outTradeNoList);
                            }}));

            notifyTaskDao.insert(notifyTask);

        }
    }

    @Override
    public boolean isSCBlackIntercept(String source, String channel) {

        return dccService.isSCBlackIntercept(
                source,
                channel
        );
    }
}
