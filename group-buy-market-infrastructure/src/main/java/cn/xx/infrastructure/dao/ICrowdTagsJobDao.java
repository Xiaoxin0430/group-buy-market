package cn.xx.infrastructure.dao;

import cn.xx.infrastructure.dao.po.CrowdTagsJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaoxin
 * @description 查询标签采集任务
 * @create 2026/8/3 9:45
 */

@Mapper
public interface ICrowdTagsJobDao {

    CrowdTagsJob queryCrowdTagsJob(
            CrowdTagsJob crowdTagsJobReq
    );

}
