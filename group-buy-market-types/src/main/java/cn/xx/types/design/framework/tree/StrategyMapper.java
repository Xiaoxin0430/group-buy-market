package cn.xx.types.design.framework.tree;

/**
 * @author xiaoxin
 * @description 策略映射器
 * T Type 入参类型
 * D Data/Domain 上下文参数
 * R Result/Response 返参类型
 * @create 2026/7/10 14:49
 */


public interface StrategyMapper<T, D, R> {

    StrategyHandler<T,D,R> get(T requestParameter, D dynamicContext);
}
