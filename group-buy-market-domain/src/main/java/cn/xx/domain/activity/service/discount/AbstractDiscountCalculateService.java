package cn.xx.domain.activity.service.discount;

import cn.xx.domain.activity.adapter.repository.IActivityRepository;
import cn.xx.domain.activity.model.valobj.DiscountTypeEnum;
import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author xiaoxin
 * @description 公共计算模板
 * @create 2026/7/17 16:29
 */

@Slf4j
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService {

    @Resource
    protected IActivityRepository repository;

    @Override
    public BigDecimal calculate(
            String userId,
            BigDecimal originalPrice,
            GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {

        // 1. 人群标签过滤
        if(DiscountTypeEnum.TAG.equals(
                groupBuyDiscount.getDiscountType())){

            boolean isCrowdRange =
                    filterTagId(
                            userId,
                            groupBuyDiscount.getTagId());

            // 用户不属于优惠人群，直接返回原价
            if (!isCrowdRange) {
                log.info(
                        "折扣优惠计算拦截，用户不在优惠人群标签范围内 userId:{}",
                        userId
                );
                return originalPrice;
            }
        }

        // 2. 折扣优惠计算
        return doCalculate(originalPrice, groupBuyDiscount);
    }

    /**
     * 检查用户是否属于指定标签人群
     */
    private boolean filterTagId(String userId, String tagId) {
        return repository.isTagCrowdRange(tagId, userId);
    }

    /**
     * 具体折扣策略实现
     */
    protected abstract BigDecimal doCalculate(
            BigDecimal originalPrice,
            GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);

}
