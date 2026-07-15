package cn.xx.domain.activity.service;

import cn.xx.domain.activity.model.entity.MarketProductEntity;
import cn.xx.domain.activity.model.entity.TrialBalanceEntity;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/14 15:22
 */


public interface IIndexGroupBuyMarketService {

    TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception;

}
