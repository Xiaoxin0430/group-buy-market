package cn.xx.test.domain.activity;

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
 * @description
 * @create 2026/7/15 15:14
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class IIndexGroupBuyMarketServiceTest {
    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;


    @Test
    public void test_indexMarketTrial() throws Exception {
        MarketProductEntity marketProductEntity = new MarketProductEntity();
        marketProductEntity.setUserId("xiaofuge");
        marketProductEntity.setSource("s01");
        marketProductEntity.setChannel("c01");
        marketProductEntity.setGoodsId("9890001");

        TrialBalanceEntity trialBalanceEntity = indexGroupBuyMarketService.indexMarketTrial(marketProductEntity);
        log.info("请求参数:{}", JSON.toJSONString(marketProductEntity));
        log.info("返回结果:{}", JSON.toJSONString(trialBalanceEntity));
    }

    @Test
    public void test_indexMarketTrial_no_sku() throws Exception {
        MarketProductEntity marketProductEntity = new MarketProductEntity();
        marketProductEntity.setUserId("xiaofuge");
        marketProductEntity.setSource("s01");
        marketProductEntity.setChannel("c01");
        marketProductEntity.setGoodsId("9890002");

        TrialBalanceEntity trialBalanceEntity = indexGroupBuyMarketService
                .indexMarketTrial(marketProductEntity);

        log.info("请求参数:{}", JSON.toJSONString(marketProductEntity));

        log.info("返回结果:{}", JSON.toJSONString(trialBalanceEntity));
    }

    /**
     * 测试人群标签功能的时候，可以进入 ITagServiceTest#test_tag_job 执行人群写入
     */
    //测试“不属于标签人群的用户”
    @Test
    public void test_indexMarketTrial_no_tag() throws Exception {
        MarketProductEntity marketProductEntity = new MarketProductEntity();

        marketProductEntity.setUserId("dacihua");
        marketProductEntity.setSource("s01");
        marketProductEntity.setChannel("c01");
        marketProductEntity.setGoodsId("9890001");

        TrialBalanceEntity trialBalanceEntity = indexGroupBuyMarketService.indexMarketTrial(marketProductEntity);

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
