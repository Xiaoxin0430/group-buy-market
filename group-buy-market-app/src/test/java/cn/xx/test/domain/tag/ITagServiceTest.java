package cn.xx.test.domain.tag;

import cn.xx.domain.tag.service.ITagService;
import cn.xx.domain.tag.service.TagService;
import cn.xx.infrastructure.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBitSet;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/3 10:54
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ITagServiceTest {
    @Resource
    private ITagService tagService;

    @Resource
    private IRedisService redisService;

    @Test
    public void test_tag_job() {
        tagService.execTagBatchJob(
                "RQ_KJHKL98UU78H66554GFDV",
                "10001"
        );
    }

    @Test
    public void test_get_tag_bitmap() {
        //获取标签对应位图
        RBitSet bitSet = redisService.getBitSet("RQ_KJHKL98UU78H66554GFDV");

        //查询用户所在位图值
        log.info("xiaofuge 存在，预期结果为 true，测试结果:{}",
                bitSet.get(
                        redisService.getIndexFromUserId("xiaofuge")
                )
        );

        log.info(
                "gudebai 不存在，预期结果为 false，测试结果:{}",
                bitSet.get(
                        redisService.getIndexFromUserId(
                                "gudebai"
                        )
                )
        );
    }
}
