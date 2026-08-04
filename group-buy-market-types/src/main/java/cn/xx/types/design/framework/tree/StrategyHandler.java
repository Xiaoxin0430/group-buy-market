package cn.xx.types.design.framework.tree;

/**
 * @author xiaoxin
 * @description 策略处理器
 * T Type 入参类型/请求参数
 * D Data/Domain 上下文参数
 * R Result/Response 返参类型/返回结果
 * @create 2026/7/10 14:51
 */


public interface StrategyHandler<T, D, R> {
    //兜底的默认处理器
    StrategyHandler DEFAULT = (T,D) -> null;

    R apply(T requestParameter, D dynamicContext) throws Exception;
}
