package cn.xx.domain.tag.service;

import cn.xx.domain.tag.adapter.repository.ITagRepository;
import cn.xx.domain.tag.model.entity.CrowdTagsJobEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/3 10:38
 */


@Slf4j
@Service
public class TagService implements ITagService {

    @Resource
    private ITagRepository tagRepository;

    @Override
    public void execTagBatchJob(String tagId, String batchId) {
        log.info("人群标签批次任务 tagId:{} batchId:{}", tagId, batchId);
        //查询批次任务
        CrowdTagsJobEntity crowdTagsJobEntity = tagRepository.queryCrowdTagsJobEntity(tagId, batchId);

        //采集用户数据-后续实现

        //写入用户
        List<String> userIdList = new ArrayList<String>() {{
                    add("xiaofuge");
                    add("liergou");
                    add("xfg01");
                    add("xfg02");
                    add("xfg03");
                }};

        for (String userId : userIdList) {
            tagRepository.addCrowdTagsUserId(tagId, userId);
        }

        //更新标签信息
        tagRepository.updateCrowdTagsStatistics(tagId,userIdList.size());
    }
}
