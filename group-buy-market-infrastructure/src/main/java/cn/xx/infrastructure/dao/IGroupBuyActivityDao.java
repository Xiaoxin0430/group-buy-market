package cn.xx.infrastructure.dao;

import cn.xx.infrastructure.dao.po.GroupBuyActivity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/9 15:37
 */

@Mapper
public interface IGroupBuyActivityDao {

    List<GroupBuyActivity> queryGroupBuyActivityList();

    //查询有效得活动信息
    GroupBuyActivity queryValidGroupBuyActivityById(
            Long activityId
    );

    //查询活动信息
    GroupBuyActivity queryGroupBuyActivityByActivityId(Long activityId);
}
