package cn.xx.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xiaoxin
 * @description 回到结果枚举
 * @create 2026/8/28 16:47
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum NotifyTaskHTTPEnumVO {

    SUCCESS("success", "成功"),
    ERROR("error", "失败"),
    NULL(null, "空执行"),
    ;

    private String code;
    private String info;

}
