package net.redborder.samza.rules.memory;

import net.redborder.samza.rules.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class QueryMemoryRule extends Rule {
    MemoryRuleManager memoryRuleManager;
    private static final Logger log = LoggerFactory.getLogger(QueryMemoryRule.class);

    public QueryMemoryRule(String ruleUuid, MemoryRuleManager memoryRuleManager) {
        super(ruleUuid);
        this.memoryRuleManager = memoryRuleManager;
    }

    @Override
    public Boolean disable(String endpoint, Map<String, Object> condition) {
        return null;
    }

    @Override
    public Boolean enable(String endpoint, Map<String, Object> condition) {
        return null;
    }

    @Override
    public Boolean verify(String endpoint, Map<String, Object> condition) {
        return  memoryRuleManager.queryRule(endpoint, ruleUuid);
    }
}
