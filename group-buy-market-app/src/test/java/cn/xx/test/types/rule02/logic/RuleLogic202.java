package cn.xx.test.types.rule02.logic;

import cn.xx.test.types.rule02.factory.Rule02TradeRuleFactory;
import cn.xx.types.design.framework.link.model2.handler.ILogicHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/8 16:40
 */

@Slf4j
@Service
public class RuleLogic202 implements ILogicHandler<String, Rule02TradeRuleFactory.DynamicContext, XxxResponse> {

    public XxxResponse apply(String requestParameter, Rule02TradeRuleFactory.DynamicContext dynamicContext) throws Exception{

        log.info("link model02 RuleLogic202");

        return new XxxResponse("hi 小傅哥！");
    }

}
