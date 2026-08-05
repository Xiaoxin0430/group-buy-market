package cn.xx.api;

import cn.xx.api.dto.LockMarketPayOrderRequestDTO;
import cn.xx.api.dto.LockMarketPayOrderResponseDTO;
import cn.xx.api.response.Response;

/**
 * @author xiaoxin
 * @description 营销交易服务接口
 * @create 2026/8/5 17:05
 */


public interface IMarketTradeService {

    //锁单并生成营销订单
    Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(
            LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO
    );

}
