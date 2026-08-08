package cn.xx.types.design.framework.link.model1;

/**
 * @author xiaoxin
 * @description 责任链节点抽象实现类
 * @create 2026/8/8 14:20
 */


public abstract class AbstractLogicLink<T, D, R> implements ILogicLink<T, D, R> {

    private ILogicLink<T, D, R> next;

    //获取下一个节点
    @Override
    public ILogicLink<T, D, R> next() {
        return next;
    }

    //连接下一个节点
    @Override
    public ILogicLink<T, D, R> appendNext(ILogicLink<T, D, R> next) {
        this.next = next;
        return next;
    }

    //直接让下一个节点进行操作
    protected R next(T requestParameter, D dynamicContext) throws Exception {
        return next.apply(requestParameter, dynamicContext);
    }


}
