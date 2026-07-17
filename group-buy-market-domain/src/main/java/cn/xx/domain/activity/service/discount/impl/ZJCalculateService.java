package cn.xx.domain.activity.service.discount.impl;

import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.xx.domain.activity.service.discount.AbstractDiscountCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/17 16:41
 */

@Slf4j
@Service("ZJ")
public class ZJCalculateService extends AbstractDiscountCalculateService {

    @Override
    public BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        log.info("优惠策略折扣计算:{}", groupBuyDiscount.getDiscountType().getCode());

        String marketExpr = groupBuyDiscount.getMarketExpr();

        BigDecimal deductionPrice = originalPrice.subtract(new BigDecimal(marketExpr));

        // 优惠后不能小于等于0，最低支付1分钱
        if (deductionPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal("0.01");
        }

        return deductionPrice;
    }
}
