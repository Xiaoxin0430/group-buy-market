package cn.xx.domain.trade.adapter.port;

import cn.xx.domain.trade.model.entity.NotifyTaskEntity;

/**
 * @author xiaoxin
 * @description 发送消息接口
 * @create 2026/8/28 16:48
 */


public interface ITradePort {

    String groupBuyNotify(NotifyTaskEntity notifyTask) throws Exception;

}
