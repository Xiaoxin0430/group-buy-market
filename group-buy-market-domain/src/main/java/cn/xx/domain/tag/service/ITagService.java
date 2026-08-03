package cn.xx.domain.tag.service;

/**
 * @author xiaoxin
 * @description 规定标签服务具备什么业务能力
 * @create 2026/8/3 10:36
 */


public interface ITagService {
    /**
     * 执行人群标签批次任务
     *
     * @param tagId   人群ID
     * @param batchId 批次ID
     */
    void execTagBatchJob(
            String tagId,
            String batchId
    );

}
