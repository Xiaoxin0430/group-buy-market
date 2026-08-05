package cn.xx.infrastructure.dao;

import cn.xx.infrastructure.dao.po.GroupBuyOrderList;
import org.apache.ibatis.annotations.Mapper;

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

}
