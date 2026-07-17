package cn.xx.domain.activity.service.discount.impl;

import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.xx.domain.activity.service.discount.AbstractDiscountCalculateService;
import cn.xx.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/17 16:45
 */

@Slf4j
@Service("MJ")
public class MJCalculateService extends AbstractDiscountCalculateService {
    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        String marketExpr = groupBuyDiscount.getMarketExpr();

        String[] split = marketExpr.split(Constants.SPLIT);

        BigDecimal x = new BigDecimal(split[0].trim());
        BigDecimal y = new BigDecimal(split[1].trim());

        // 没达到门槛，不享受满减
        if (originalPrice.compareTo(x) < 0) {
            return originalPrice;
        }

        BigDecimal deductionPrice = originalPrice.subtract(y);

        if (deductionPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal("0.01");
        }

        return deductionPrice;    }
}
