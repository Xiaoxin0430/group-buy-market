package cn.xx.domain.activity.service;

import cn.xx.domain.activity.adapter.repository.IActivityRepository;
import cn.xx.domain.activity.model.entity.MarketProductEntity;
import cn.xx.domain.activity.model.entity.TrialBalanceEntity;
import cn.xx.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import cn.xx.domain.activity.model.valobj.TeamStatisticVO;
import cn.xx.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.xx.types.design.framework.tree.StrategyHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/14 15:22
 */

@Service
public class IndexGroupBuyMarketServiceImpl implements IIndexGroupBuyMarketService{

    @Resource
    private DefaultActivityStrategyFactory defaultActivityStrategyFactory;

    @Resource
    private IActivityRepository repository;


    @Override
    public TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception {

        //从工厂取得工程树入口  获取执行策略
        StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> strategyHandler =
                defaultActivityStrategyFactory.strategyHandler();

        //试算操作
        return strategyHandler.apply(
                marketProductEntity,
                new DefaultActivityStrategyFactory.DynamicContext()
        );
    }

    @Override
    public List<UserGroupBuyOrderDetailEntity> queryInProgressUserGroupBuyOrderDetailList(
            Long activityId, String userId, Integer ownerCount, Integer randomCount) {

        List<UserGroupBuyOrderDetailEntity> unionAllList = new ArrayList<>();

        // 第一部分：当前用户已参与的队伍，首页置顶展示。
        if (0 != ownerCount) {
            List<UserGroupBuyOrderDetailEntity> ownerList =
                    repository.queryInProgressUserGroupBuyOrderDetailListByOwner(activityId, userId, ownerCount);
            if (null != ownerList && !ownerList.isEmpty()) {
                unionAllList.addAll(ownerList);
            }
        }

        // 第二部分：其他用户的可参与队伍。
        if (0 != randomCount) {
            List<UserGroupBuyOrderDetailEntity> randomList =
                    repository.queryInProgressUserGroupBuyOrderDetailListByRandom(
                            activityId, userId, randomCount);
            if (null != randomList && !randomList.isEmpty()) {
                unionAllList.addAll(randomList);
            }
        }

        return unionAllList;
    }

    @Override
    public TeamStatisticVO queryTeamStatisticByActivityId(Long activityId) {
        return repository.queryTeamStatisticByActivityId(activityId);
    }
}
