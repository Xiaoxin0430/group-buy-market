package cn.xx.domain.trade.model.aggregate;

import cn.xx.domain.trade.model.entity.PayActivityEntity;
import cn.xx.domain.trade.model.entity.PayDiscountEntity;
import cn.xx.domain.trade.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoxin
 * @description 拼团订单聚合对象
 * @create 2026/8/5 15:51
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyOrderAggregate {

    /** 用户实体对象 */
    private UserEntity userEntity;

    /** 支付活动实体对象 */
    private PayActivityEntity payActivityEntity;

    /** 支付优惠实体对象 */
    private PayDiscountEntity payDiscountEntity;

    /** 已参与拼团量 */
    private Integer userTakeOrderCount;

}
