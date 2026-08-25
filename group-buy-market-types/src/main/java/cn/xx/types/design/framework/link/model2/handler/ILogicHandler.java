package cn.xx.types.design.framework.link.model2.handler;

/**
 * @author xiaoxin
 * @description 业务逻辑处理器统一接口。
 * 定义规则格式
 * @create 2026/8/8 15:32
 */


public interface ILogicHandler<T, D, R> {

    //不需要知道下一个节点是谁，只要没有给出最后的结果，就继续往下执行
    default R next(T requestParameter, D dynamicContext) {
        return null;
    }

    R apply(T requestParameter, D dynamicContext) throws Exception;
}
