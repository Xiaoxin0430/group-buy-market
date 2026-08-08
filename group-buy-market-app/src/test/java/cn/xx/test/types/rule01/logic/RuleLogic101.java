package cn.xx.test.types.rule01.logic;

import cn.xx.test.types.rule01.factory.Rule01TradeRuleFactory;
import cn.xx.types.design.framework.link.model1.AbstractLogicLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/8 14:34
 */

@Slf4j
@Service
public class RuleLogic101 extends AbstractLogicLink<String, Rule01TradeRuleFactory.DynamicContext, String> {

    @Override
    public String apply(String requestParameter, Rule01TradeRuleFactory.DynamicContext dynamicContext) throws Exception {
        log.info("link model01 RuleLogic101");

        return next(requestParameter, dynamicContext);
    }
}
