package cn.xx.api;

import cn.xx.api.dto.GoodsMarketRequestDTO;
import cn.xx.api.dto.GoodsMarketResponseDTO;
import cn.xx.api.response.Response;

/**
 * @author xiaoxin
 * @description 营销首页服务接口
 * @create 2026/9/1 19:24
 */


public interface IMarketIndexService {

    /**
     * 查询拼团营销配置
     *
     * @param goodsMarketRequestDTO 营销商品信息
     * @return 营销配置信息
     */
    Response<GoodsMarketResponseDTO> queryGroupBuyMarketConfig(GoodsMarketRequestDTO goodsMarketRequestDTO);

}

