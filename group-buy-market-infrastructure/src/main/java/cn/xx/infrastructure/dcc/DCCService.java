package cn.xx.infrastructure.dcc;

import cn.xx.types.annotations.DCCValue;
import org.springframework.stereotype.Service;

/**
 * @author xiaoxin
 * @description 动态配置服务
 * @create 2026/8/4 20:36
 */

@Service
public class DCCService {
    /**
     * 降级开关 0关闭、1开启
     */
    @DCCValue("downgradeSwitch:0")
    private String downgradeSwitch;
    /**
     * 灰度配置 100：默认全部放行
     */
    @DCCValue("cutRange:100")
    private String cutRange;

    public boolean isDowngradeSwitch() {
        return "1".equals(downgradeSwitch);
    }

    public boolean isCutRange(String userId){
        int hashCode = Math.abs(userId.hashCode());
        int lastTwoDigits = hashCode % 100;

        if (lastTwoDigits <= Integer.parseInt(cutRange)) {
            return true;
        }
        return false;
    }

}
