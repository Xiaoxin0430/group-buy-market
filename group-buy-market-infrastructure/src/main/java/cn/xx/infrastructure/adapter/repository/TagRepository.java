package cn.xx.infrastructure.adapter.repository;

import cn.xx.domain.tag.adapter.repository.ITagRepository;
import cn.xx.domain.tag.model.entity.CrowdTagsJobEntity;
import cn.xx.infrastructure.dao.ICrowdTagsDao;
import cn.xx.infrastructure.dao.ICrowdTagsDetailDao;
import cn.xx.infrastructure.dao.ICrowdTagsJobDao;
import cn.xx.infrastructure.dao.po.CrowdTags;
import cn.xx.infrastructure.dao.po.CrowdTagsDetail;
import cn.xx.infrastructure.dao.po.CrowdTagsJob;
import cn.xx.infrastructure.redis.IRedisService;
import org.redisson.api.RBitSet;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/3 10:14
 */

@Repository
public class TagRepository implements ITagRepository {

    @Resource
    private ICrowdTagsDao crowdTagsDao;

    @Resource
    private ICrowdTagsDetailDao crowdTagsDetailDao;

    @Resource
    private ICrowdTagsJobDao crowdTagsJobDao;

    @Resource
    private IRedisService redisService;

    //查询标签任务
    @Override
    public CrowdTagsJobEntity queryCrowdTagsJobEntity(String tagId, String batchId) {

        CrowdTagsJob crowdTagsJobReq = new CrowdTagsJob();
        crowdTagsJobReq.setTagId(tagId);
        crowdTagsJobReq.setBatchId(batchId);

        CrowdTagsJob crowdTagsJobRes = crowdTagsJobDao.queryCrowdTagsJob(crowdTagsJobReq);
        if (null == crowdTagsJobRes) {
            return null;
        }

        return CrowdTagsJobEntity.builder()
                .tagType(crowdTagsJobRes.getTagType())
                .tagRule(crowdTagsJobRes.getTagRule())
                .statStartTime(
                        crowdTagsJobRes.getStatStartTime()
                )
                .statEndTime(
                        crowdTagsJobRes.getStatEndTime()
                )
                .build();
    }

    //添加标签用户
    @Override
    public void addCrowdTagsUserId(String tagId, String userId) {

        CrowdTagsDetail detail = new CrowdTagsDetail();
        detail.setTagId(tagId);
        detail.setUserId(userId);

        try {
            crowdTagsDetailDao.addCrowdTagsUserId(detail);

            RBitSet bitSet = redisService.getBitSet(tagId);
            int index = redisService.getIndexFromUserId(userId);
            bitSet.set(index,true);
        } catch (DuplicateKeyException ignore) {
            // 忽略唯一索引冲突
        }
    }

    @Override
    public void updateCrowdTagsStatistics(String tagId, int count) {
        CrowdTags crowdTagsReq = new CrowdTags();
        crowdTagsReq.setTagId(tagId);
        crowdTagsReq.setStatistics(count);

        crowdTagsDao.updateCrowdTagsStatistics(crowdTagsReq);

    }
}
