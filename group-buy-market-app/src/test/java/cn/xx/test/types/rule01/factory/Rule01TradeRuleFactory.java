package cn.xx.test.types.rule01.factory;

import cn.xx.test.types.rule01.logic.RuleLogic101;
import cn.xx.test.types.rule01.logic.RuleLogic102;
import cn.xx.types.design.framework.link.model1.ILogicLink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/8 14:48
 */

@Service
public class Rule01TradeRuleFactory {

    @Resource
    private RuleLogic101 ruleLogic101;

    @Resource
    private RuleLogic102 ruleLogic102;

    //整条链的入口节点,从101节点开始
    public ILogicLink<String, DynamicContext, String> openLogicLink() {
        ruleLogic101.appendNext(ruleLogic102);
        return ruleLogic101;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {
        private String age;
    }


}
