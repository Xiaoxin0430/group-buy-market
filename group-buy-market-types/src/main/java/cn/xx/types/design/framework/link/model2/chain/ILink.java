package cn.xx.types.design.framework.link.model2.chain;

/**
 * @author xiaoxin
 * @description 链接口(规定一条链表至少需要具备哪些操作)
 * @create 2026/8/8 15:40
 */


public interface ILink<E> {

    boolean add(E e);

    boolean addFirst(E e);

    boolean addLast(E e);

    boolean remove(Object o);

    E get(int index);

    void printLinkList();

}
