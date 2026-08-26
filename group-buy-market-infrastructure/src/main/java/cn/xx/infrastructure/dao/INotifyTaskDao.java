package cn.xx.infrastructure.dao;

import cn.xx.infrastructure.dao.po.NotifyTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaoxin
 * @description 回调任务
 * @create 2026/8/26 14:18
 */


@Mapper
public interface INotifyTaskDao {

    void insert(NotifyTask notifyTask);

}
