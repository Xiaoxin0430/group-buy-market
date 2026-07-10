package cn.xx.test.infrastructure.dao;

import cn.xx.infrastructure.dao.IGroupBuyActivityDao;
import cn.xx.infrastructure.dao.po.GroupBuyActivity;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/10 13:48
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupBuyActivityDaoTest {

    @Resource
    private IGroupBuyActivityDao iGroupBuyActivityDao;

    @Test
    public void test(){
        List<GroupBuyActivity> groupBuyActivities = iGroupBuyActivityDao.queryGroupBuyActivityList();
        log.info("测试结果：{}", JSON.toJSONString(groupBuyActivities.toString()));
    }
}
