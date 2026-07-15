package cn.xx.types.design.framework.tree;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaoxin
 * @description
 * 实现策略映射和处理接口
 * 根据get方法返回需要的处理器
 * 处理器不空就正常调用其apply方法，为空就调用默认的处理器
 * @create 2026/7/10 15:09
 */


public abstract class AbstractStrategyRouter<T, D, R> implements StrategyMapper<T, D, R>, StrategyHandler<T, D, R> {

    @Getter
    @Setter
    StrategyHandler<T, D, R> defaultStrategyHandler = StrategyHandler.DEFAULT;

    public R router(T requestParameter, D dynamicContext) throws Exception {
        StrategyHandler<T,D,R> strategyHandler = get(requestParameter, dynamicContext);
        if (strategyHandler != null) {
            return strategyHandler.apply(requestParameter, dynamicContext);
        }
        return defaultStrategyHandler.apply(requestParameter, dynamicContext);
    }
}
