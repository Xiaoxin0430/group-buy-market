package cn.xx.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xiaoxin
 * @description 队伍统计值对象
 * @create 2026/9/1 20:30
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamStatisticVO {
    /** 开团队伍数量 */
    private Integer allTeamCount;

    /** 已成团队伍数量 */
    private Integer allTeamCompleteCount;

    /** 所有队伍锁单人数之和 */
    private Integer allTeamUserCount;
}
