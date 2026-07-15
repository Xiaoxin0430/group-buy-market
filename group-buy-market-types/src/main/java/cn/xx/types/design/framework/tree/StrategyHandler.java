package cn.xx.types.design.framework.tree;

/**
 * @author xiaoxin
 * @description 策略处理器
 * T Type 入参类型
 * D Data/Domain 上下文参数
 * R Result/Response 返参类型
 * @create 2026/7/10 14:51
 */


public interface StrategyHandler<T, D, R> {

    StrategyHandler DEFAULT = (T,D) -> null;

    R apply(T requestParameter, D dynamicContext) throws Exception;
}
