package cn.xx.test.domain.trade;

import cn.xx.domain.trade.adapter.repository.ITradeRepository;
import cn.xx.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.xx.domain.trade.model.entity.TradeRuleCommandEntity;
import cn.xx.domain.trade.model.entity.TradeRuleFilterBackEntity;
import cn.xx.domain.trade.service.factory.TradeRuleFilterFactory;
import cn.xx.domain.trade.service.filter.ActivityUsabilityRuleFilter;
import cn.xx.domain.trade.service.filter.UserTakeLimitRuleFilter;
import cn.xx.types.design.framework.link.model2.LinkArmory;
import cn.xx.types.design.framework.link.model2.chain.BusinessLinkedList;
import cn.xx.types.enums.ActivityStatusEnumVO;
import cn.xx.types.enums.ResponseCode;
import cn.xx.types.exception.AppException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 交易规则责任链的单元测试，不依赖数据库或 Redis。
 */
public class TradeRuleFilterTest {

    private ITradeRepository repository;
    private BusinessLinkedList<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity> ruleFilter;

    @Before
    public void setUp() {
        repository = mock(ITradeRepository.class);

        ActivityUsabilityRuleFilter activityFilter = new ActivityUsabilityRuleFilter();
        UserTakeLimitRuleFilter takeLimitFilter = new UserTakeLimitRuleFilter();
        ReflectionTestUtils.setField(activityFilter, "repository", repository);
        ReflectionTestUtils.setField(takeLimitFilter, "repository", repository);

        ruleFilter = new LinkArmory<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity>(
                "交易规则过滤链", activityFilter, takeLimitFilter).getLogicLink();
    }

    @Test
    public void shouldReturnUserParticipationCountWhenActivityIsAvailable() throws Exception {
        Long activityId = 100123L;
        String userId = "user-001";
        when(repository.queryGroupBuyActivityEntityByActivityId(activityId)).thenReturn(activity(activityId, 3, ActivityStatusEnumVO.EFFECTIVE));
        when(repository.queryOrderCountByActivityId(activityId, userId)).thenReturn(2);

        TradeRuleFilterBackEntity result = ruleFilter.apply(command(userId, activityId), new TradeRuleFilterFactory.DynamicContext());

        assertEquals(Integer.valueOf(2), result.getUserTakeOrderCount());
        verify(repository).queryGroupBuyActivityEntityByActivityId(activityId);
        verify(repository).queryOrderCountByActivityId(activityId, userId);
    }

    @Test
    public void shouldRejectAnInactiveActivityBeforeCheckingUserOrderCount() throws Exception {
        Long activityId = 100123L;
        String userId = "user-001";
        when(repository.queryGroupBuyActivityEntityByActivityId(activityId)).thenReturn(activity(activityId, 3, ActivityStatusEnumVO.CREATE));

        assertBusinessError(ResponseCode.E0101, userId, activityId);

        verify(repository, never()).queryOrderCountByActivityId(activityId, userId);
    }

    @Test
    public void shouldRejectAUserWhoReachedTheParticipationLimit() throws Exception {
        Long activityId = 100123L;
        String userId = "user-001";
        when(repository.queryGroupBuyActivityEntityByActivityId(activityId)).thenReturn(activity(activityId, 2, ActivityStatusEnumVO.EFFECTIVE));
        when(repository.queryOrderCountByActivityId(activityId, userId)).thenReturn(2);

        assertBusinessError(ResponseCode.E0103, userId, activityId);
    }

    private void assertBusinessError(ResponseCode expected, String userId, Long activityId) throws Exception {
        try {
            ruleFilter.apply(command(userId, activityId), new TradeRuleFilterFactory.DynamicContext());
            fail("Expected AppException");
        } catch (AppException e) {
            assertEquals(expected.getCode(), e.getCode());
        }
    }

    private TradeRuleCommandEntity command(String userId, Long activityId) {
        return TradeRuleCommandEntity.builder().userId(userId).activityId(activityId).build();
    }

    private GroupBuyActivityEntity activity(Long activityId, Integer takeLimitCount, ActivityStatusEnumVO status) {
        Date now = new Date();
        return GroupBuyActivityEntity.builder()
                .activityId(activityId)
                .takeLimitCount(takeLimitCount)
                .status(status)
                .startTime(new Date(now.getTime() - 60_000L))
                .endTime(new Date(now.getTime() + 60_000L))
                .build();
    }
}
