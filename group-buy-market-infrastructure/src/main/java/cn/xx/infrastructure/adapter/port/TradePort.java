package cn.xx.infrastructure.adapter.port;

import cn.xx.domain.trade.adapter.port.ITradePort;
import cn.xx.domain.trade.model.entity.NotifyTaskEntity;
import cn.xx.infrastructure.gateway.GroupBuyNotifyService;
import cn.xx.infrastructure.redis.IRedisService;
import cn.xx.types.enums.NotifyTaskHTTPEnumVO;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/28 16:49
 */

@Service
public class TradePort implements ITradePort {

    @Resource
    private GroupBuyNotifyService groupBuyNotifyService;

    @Resource
    private IRedisService redisService;

    @Override
    public String groupBuyNotify(NotifyTaskEntity notifyTask) throws Exception {

        RLock lock = redisService.getLock(notifyTask.lockKey());

        try {
            // 多台应用服务器可能同时执行同一个通知任务，
            // 通过分布式锁抢占执行权
            if (lock.tryLock(3, 0, TimeUnit.SECONDS)) {

                try {
                    // 无效 notifyUrl 直接认为成功
                    if (StringUtils.isBlank(notifyTask.getNotifyUrl())
                            || "暂无".equals(notifyTask.getNotifyUrl())) {

                        return NotifyTaskHTTPEnumVO.SUCCESS
                                .getCode();
                    }

                    return groupBuyNotifyService.groupBuyNotify(
                            notifyTask.getNotifyUrl(),
                            notifyTask.getParameterJson());

                } finally {
                    if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }

            return NotifyTaskHTTPEnumVO.NULL.getCode();

        } catch (Exception e) {

            Thread.currentThread().interrupt();

            return NotifyTaskHTTPEnumVO.NULL.getCode();
        }
    }

}

