package cn.xx.test.domain.trade;

import cn.xx.domain.trade.model.entity.TradePaySettlementEntity;
import cn.xx.domain.trade.model.entity.TradePaySuccessEntity;
import cn.xx.domain.trade.service.ITradeSettlementOrderService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiaoxin
 * @description 拼团交易结算服务测试
 * @create 2026/8/26 14:46
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeSettlementOrderServiceTest {

    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;


    @Test
    public void test_settlementMarketPayOrder() throws Exception {

        TradePaySuccessEntity tradePaySuccessEntity =
                new TradePaySuccessEntity();

        tradePaySuccessEntity.setSource("s01");
        tradePaySuccessEntity.setChannel("c01");
        tradePaySuccessEntity.setUserId("xfg04");
        tradePaySuccessEntity.setOutTradeNo("075605651839");

        // 2-13 新增
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        tradePaySuccessEntity.setOutTradeTime(
                dateFormat.parse("2025-01-30 12:00:00")
        );
//        tradePaySuccessEntity.setOutTradeTime(new Date());

        TradePaySettlementEntity tradePaySettlementEntity =
                tradeSettlementOrderService
                        .settlementMarketPayOrder(
                                tradePaySuccessEntity
                        );

        log.info(
                "请求参数:{}",
                JSON.toJSONString(tradePaySuccessEntity)
        );

        log.info(
                "测试结果:{}",
                JSON.toJSONString(tradePaySettlementEntity)
        );
    }


}
