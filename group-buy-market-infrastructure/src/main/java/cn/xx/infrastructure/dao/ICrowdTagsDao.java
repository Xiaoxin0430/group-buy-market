package cn.xx.infrastructure.dao;

import cn.xx.infrastructure.dao.po.CrowdTags;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaoxin
 * @description 更新标签统计量
 * @create 2026/8/3 9:50
 */

@Mapper
public interface ICrowdTagsDao {

    void updateCrowdTagsStatistics(
            CrowdTags crowdTagsReq
    );

}
