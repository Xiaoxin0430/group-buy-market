package cn.xx.types.design.framework.link.model1;

/**
 * @author xiaoxin
 * @description 责任链装配(规定节点连接方式)
 * @create 2026/8/8 14:09
 */


public interface ILogicChainArmory<T,D,R> {
    //获取下一个节点
    ILogicLink<T, D, R> next();

    //设置下一个节点
    ILogicLink<T, D, R> appendNext( ILogicLink<T, D, R> next );


}
