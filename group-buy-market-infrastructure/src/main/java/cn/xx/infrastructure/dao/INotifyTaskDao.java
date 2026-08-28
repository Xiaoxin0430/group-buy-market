package cn.xx.infrastructure.dao;

import cn.xx.infrastructure.dao.po.NotifyTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiaoxin
 * @description 回调任务
 * @create 2026/8/26 14:18
 */


@Mapper
public interface INotifyTaskDao {

    void insert(NotifyTask notifyTask);

    List<NotifyTask> queryUnExecutedNotifyTaskList();

    NotifyTask queryUnExecutedNotifyTaskByTeamId(String teamId);

    int updateNotifyTaskStatusSuccess(String teamId);

    int updateNotifyTaskStatusError(String teamId);

    int updateNotifyTaskStatusRetry(String teamId);

}
