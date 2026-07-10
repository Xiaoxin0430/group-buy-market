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

}
