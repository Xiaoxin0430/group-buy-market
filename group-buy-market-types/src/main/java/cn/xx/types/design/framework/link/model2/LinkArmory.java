package cn.xx.types.design.framework.link.model2;

import cn.xx.types.design.framework.link.model2.chain.BusinessLinkedList;
import cn.xx.types.design.framework.link.model2.handler.ILogicHandler;

/**
 * @author xiaoxin
 * @description 链路装配
 * @create 2026/8/8 16:32
 */


public class LinkArmory<T, D, R> {

    private final BusinessLinkedList<T, D, R> logicLink;

    @SafeVarargs
    public LinkArmory(String linkName, ILogicHandler<T, D, R>... logicHandlers) {
        logicLink = new BusinessLinkedList<>(linkName);
        for (ILogicHandler<T, D, R> logicHandler: logicHandlers){
            logicLink.add(logicHandler);
        }
    }

    public BusinessLinkedList<T, D, R> getLogicLink() {
        return logicLink;
    }

}
