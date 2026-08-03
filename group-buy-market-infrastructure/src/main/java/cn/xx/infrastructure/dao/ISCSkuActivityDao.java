package cn.xx.infrastructure.dao;

import cn.xx.infrastructure.dao.po.SCSkuActivity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/3 17:39
 */

@Mapper
public interface ISCSkuActivityDao {

    SCSkuActivity querySCSkuActivityBySCGoodsId(
            SCSkuActivity scSkuActivity
    );

}
