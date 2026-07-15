package cn.xx.infrastructure.dao;

import cn.xx.infrastructure.dao.po.Sku;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/15 14:05
 */

@Mapper
public interface ISkuDao {

    Sku querySkuByGoodsId(String goodsid);
}
