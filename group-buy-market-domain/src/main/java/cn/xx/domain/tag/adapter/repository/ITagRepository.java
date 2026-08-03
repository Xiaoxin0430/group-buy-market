package cn.xx.domain.tag.adapter.repository;

import cn.xx.domain.tag.model.entity.CrowdTagsJobEntity;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/3 9:57
 */


public interface ITagRepository {

    CrowdTagsJobEntity queryCrowdTagsJobEntity(
            String tagId,
            String batchId
    );

    void addCrowdTagsUserId(
            String tagId,
            String userId
    );

    void updateCrowdTagsStatistics(
            String tagId,
            int count
    );

}
