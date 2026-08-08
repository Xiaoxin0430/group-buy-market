package cn.xx.types.design.framework.link.model1;

/**
 * @author xiaoxin
 * @description 规则责任链接口(规定每个节点有什么方法)
 * @create 2026/8/8 14:15
 */


public interface ILogicLink<T,D,R> extends ILogicChainArmory<T,D,R>{

    R apply(T requestParameter, D dynamicContext) throws Exception;
}
