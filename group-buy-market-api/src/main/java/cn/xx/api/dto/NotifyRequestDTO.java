package cn.xx.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @author xiaoxin
 * @description 回调请求对象/商城接收拼团系统通知时的请求参数
 * @create 2026/8/28 18:54
 */

@Data
public class NotifyRequestDTO {

    /** 组队ID */
    private String teamId;

    /** 外部单号 */
    private List<String> outTradeNoList;

}
