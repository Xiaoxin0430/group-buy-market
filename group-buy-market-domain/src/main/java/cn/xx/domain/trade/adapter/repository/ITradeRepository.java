package cn.xx.domain.trade.adapter.repository;

import cn.xx.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import cn.xx.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import cn.xx.domain.trade.model.entity.*;
import cn.xx.domain.trade.model.valobj.GroupBuyProgressVO;
import cn.xx.domain.trade.model.entity.GroupBuyActivityEntity;

import java.util.List;

/**
 * @author xiaoxin
 * @description 交易仓储服务接口
 * @create 2026/8/5 16:06
 */


public interface ITradeRepository {

    //查询已有的营销订单
    MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(
            String userId,
            String outTradeNo
    );

    //锁定营销订单
    MarketPayOrderEntity lockMarketPayOrder(
            GroupBuyOrderAggregate groupBuyOrderAggregate
    );

    //查询拼团进度
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    //根据活动id查询活动业务信息
    GroupBuyActivityEntity queryGroupBuyActivityEntityByActivityId(Long activityId);

    //查询用户在这个活动下单情况
    Integer queryOrderCountByActivityId(Long activityId, String userId);

    //根据team id查询拼团团队
    GroupBuyTeamEntity queryGroupBuyTeamByTeamId(String teamId);

    //拼团支付结算
    boolean settlementMarketPayOrder(
            GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate
    );

    //渠道来源判断
    boolean isSCBlackIntercept(String source, String channel);

    //查询/更新回调通知
    List<NotifyTaskEntity> queryUnExecutedNotifyTaskList();

    List<NotifyTaskEntity> queryUnExecutedNotifyTaskList(String teamId);

    int updateNotifyTaskStatusSuccess(String teamId);

    int updateNotifyTaskStatusError(String teamId);

    int updateNotifyTaskStatusRetry(String teamId);

}
