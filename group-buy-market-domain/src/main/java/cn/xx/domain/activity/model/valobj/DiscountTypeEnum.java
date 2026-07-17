package cn.xx.domain.activity.model.valobj;

import lombok.*;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/17 15:09
 */

@Getter
@AllArgsConstructor
public enum DiscountTypeEnum {

    BASE(0, "基础优惠"),
    TAG(1, "人群标签");

    private final Integer code;
    private final String info;

    public static DiscountTypeEnum get(Integer code) {
        switch (code) {
            case 0:
                return BASE;
            case 1:
                return TAG;
            default:
                throw new RuntimeException("不存在对应的折扣类型，code：" + code);
        }
    }


}
