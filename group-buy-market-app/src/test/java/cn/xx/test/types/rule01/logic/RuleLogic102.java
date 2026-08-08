package cn.xx.test.types.rule01.logic;

import cn.xx.test.types.rule01.factory.Rule01TradeRuleFactory;
import cn.xx.types.design.framework.link.model1.AbstractLogicLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/8 14:37
 */

@Slf4j
@Service
public class RuleLogic102 extends AbstractLogicLink<String, Rule01TradeRuleFactory.DynamicContext, String> {
    @Override
    public String apply(String requestParameter, Rule01TradeRuleFactory.DynamicContext dynamicContext) throws Exception {
        log.info("link model01 RuleLogic102");

        return "link model01 单实例链";
    }
}
