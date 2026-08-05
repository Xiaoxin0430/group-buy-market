package cn.xx.types.annotations;

import java.lang.annotation.*;


/**
 * @author xiaoxin
 * @description 注解，动态中心配置标记
 * @create 2026/8/4 18:45
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DCCValue {
    //注解接收一个字符串参数，默认为空
    String value() default "";

}
