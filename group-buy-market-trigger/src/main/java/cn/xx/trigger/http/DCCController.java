package cn.xx.trigger.http;

import cn.xx.api.IDCCService;
import cn.xx.api.response.Response;
import cn.xx.types.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author xiaoxin
 * @description 动态配置管理
 * @create 2026/8/4 21:08
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/gbm/dcc/")
public class DCCController implements IDCCService {

    @Resource
    private RTopic dccTopic;

//    curl "http://127.0.0.1:8091/api/v1/gbm/dcc/update_config?key=downgradeSwitch&value=1"
//    curl "http://127.0.0.1:8091/api/v1/gbm/dcc/update_config?key=cutRange&value=20"
    @RequestMapping(
            value = "update_config",
            method = RequestMethod.GET
    )
    @Override
    public Response<Boolean> updateConfig(
            @RequestParam String key,
            @RequestParam String value
    ) {

        try {
            log.info("DCC 动态配置值变更 key:{} value:{}", key, value);

            dccTopic.publish(key + "," + value);

            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .build();

        } catch (Exception e) {
            log.error("DCC 动态配置值变更失败 key:{} value:{}", key, value, e);

            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

}
