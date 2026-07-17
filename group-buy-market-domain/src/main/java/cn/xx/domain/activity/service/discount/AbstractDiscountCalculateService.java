package cn.xx.domain.activity.service.discount;

import cn.xx.domain.activity.model.valobj.DiscountTypeEnum;
import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.math.BigDecimal;

/**
 * @author xiaoxin
 * @description 公共计算模板
 * @create 2026/7/17 16:29
 */


public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService {

    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {

        // 1. 限定人群优惠，需要先检查用户资格
        if(DiscountTypeEnum.TAG.equals(groupBuyDiscount.getDiscountType())){
            boolean isCrowdRange = filterTagId(userId,groupBuyDiscount.getTagId());

            // 用户不属于优惠人群，直接返回原价
            if (!isCrowdRange) {
                return originalPrice;
            }
        }

        // 2. 用户有资格，执行具体折扣算法
        return doCalculate(originalPrice, groupBuyDiscount);
    }

    /**
     * 检查用户是否属于指定标签人群
     */
    private boolean filterTagId(String userId, String tagId) {
        //后续实现，目前全通过
        return true;
    }

    /**
     * 具体折扣策略实现
     */
    protected abstract BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);
}
