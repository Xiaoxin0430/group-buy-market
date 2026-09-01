package cn.xx.infrastructure.dao;

import cn.xx.infrastructure.dao.po.GroupBuyOrderList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiaoxin
 * @description 用户拼单明细dao
 * @create 2026/8/5 15:29
 */


@Mapper
public interface IGroupBuyOrderListDao {

    //保存某个用户的拼团订单明细
    void insert(GroupBuyOrderList groupBuyOrderListReq);

    //根据用户 ID 和外部交易单号，检查该用户是否已经创建过锁单订单
    GroupBuyOrderList queryGroupBuyOrderRecordByOutTradeNo(
            GroupBuyOrderList groupBuyOrderListReq
    );

    //查询用户参加多少次
    Integer queryOrderCountByActivityId(
            GroupBuyOrderList groupBuyOrderListReq
    );

    //支付完成
    int updateOrderStatus2COMPLETE(
            GroupBuyOrderList groupBuyOrderListReq);

    //找到团队所有支付的订单
    List<String> queryGroupBuyCompleteOrderOutTradeNoListByTeamId(
            String teamId);

    List<GroupBuyOrderList> queryInProgressUserGroupBuyOrderDetailListByUserId(GroupBuyOrderList groupBuyOrderListReq);

    List<GroupBuyOrderList> queryInProgressUserGroupBuyOrderDetailListByRandom(GroupBuyOrderList groupBuyOrderListReq);

    List<GroupBuyOrderList> queryInProgressUserGroupBuyOrderDetailListByActivityId(Long activityId);
}
