package cn.xx.infrastructure.dao;

import cn.xx.infrastructure.dao.po.CrowdTagsDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaoxin
 * @description 插入标签用户明细
 * @create 2026/8/3 9:50
 */

@Mapper
public interface ICrowdTagsDetailDao {
    void addCrowdTagsUserId(
            CrowdTagsDetail crowdTagsDetailReq
    );

}
