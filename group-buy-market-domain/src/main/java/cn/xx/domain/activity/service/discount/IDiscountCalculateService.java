package cn.xx.domain.activity.service.discount;

import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.math.BigDecimal;

/**
 * @author xiaoxin
 * @description 折扣计算服务接口
 * @create 2026/7/17 16:18
 */



public interface IDiscountCalculateService {
    /**
     * 计算优惠后的最终价格
     *
     * @param userId           用户 ID
     * @param originalPrice    商品原价
     * @param groupBuyDiscount 折扣配置
     * @return 优惠后的最终价格
     */

    BigDecimal calculate(
            String userId,
            BigDecimal originalPrice,
            GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount
    );
}
