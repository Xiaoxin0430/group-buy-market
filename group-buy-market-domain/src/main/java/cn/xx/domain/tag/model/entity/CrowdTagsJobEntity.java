package cn.xx.domain.tag.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xiaoxin
 * @description 领域任务对象,规定业务需要什么样的数据
 * @create 2026/8/3 9:57
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrowdTagsJobEntity {

    /** 标签类型（参与量、消费金额） */
    private Integer tagType;

    /** 标签规则（限定类型 N次） */
    private String tagRule;

    /** 统计数据，开始时间 */
    private Date statStartTime;

    /** 统计数据，结束时间 */
    private Date statEndTime;

}