package cn.xx.infrastructure.dao;

import cn.xx.infrastructure.dao.po.GroupBuyOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaoxin
 * @description 拼团队伍dao
 * @create 2026/8/5 15:24
 */

@Mapper
public interface IGroupBuyOrderDao {

    //创建拼团队伍
    void insert(GroupBuyOrder groupBuyOrder);

    //加入已有团
    int updateAddLockCount(String teamId);

    //释放锁单人数
    int updateSubtractionLockCount(String teamId);

    //查询拼团队伍进度
    GroupBuyOrder queryGroupBuyProgress(String teamId);

    //查询团队当前状态
    GroupBuyOrder queryGroupBuyTeamByTeamId(String teamId);

    //增加支付完成人数
    int updateAddCompleteCount(String teamId);

    //团队变为已完成
    int updateOrderStatus2COMPLETE(String teamId);
}
