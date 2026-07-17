package cn.xx.domain.activity.service.discount.impl;

import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.xx.domain.activity.service.discount.AbstractDiscountCalculateService;

import java.math.BigDecimal;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/17 16:49
 */


public class NCalculateService extends AbstractDiscountCalculateService {
    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        String marketExpr = groupBuyDiscount.getMarketExpr();

        return new BigDecimal(marketExpr);    }
}
