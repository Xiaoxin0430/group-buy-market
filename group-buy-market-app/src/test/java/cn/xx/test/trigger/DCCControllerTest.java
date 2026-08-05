package cn.xx.test.trigger;

import cn.xx.api.IDCCService;
import cn.xx.domain.activity.model.entity.MarketProductEntity;
import cn.xx.domain.activity.model.entity.TrialBalanceEntity;
import cn.xx.domain.activity.service.IIndexGroupBuyMarketService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xiaoxin
 * @description 动态配置管理测试
 * @create 2026/8/5 14:26
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DCCControllerTest {

    @Resource
    private IDCCService dccService;

    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;

    @Test
    public void test_updateConfig() throws InterruptedException {
        // 动态调整配置
        dccService.updateConfig("downgradeSwitch", "1");

        Thread.sleep(1000);

    }

    @Test
    public void test_updateConfig2indexMarketTrial() throws Exception {
        // 动态调整配置
        dccService.updateConfig("downgradeSwitch", "1");

        // 超时等待异步
        Thread.sleep(1000);

        // 营销验证
        MarketProductEntity marketProductEntity =
                new MarketProductEntity();

        marketProductEntity.setUserId("xiaofuge");
        marketProductEntity.setSource("s01");
        marketProductEntity.setChannel("c01");
        marketProductEntity.setGoodsId("9890001");

        TrialBalanceEntity trialBalanceEntity =
                indexGroupBuyMarketService
                        .indexMarketTrial(marketProductEntity);

        log.info(
                "请求参数:{}",
                JSON.toJSONString(marketProductEntity)
        );

        log.info(
                "返回结果:{}",
                JSON.toJSONString(trialBalanceEntity)
        );
    }

}