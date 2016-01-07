package net.redborder.samza.rules.memory;


import net.redborder.samza.rules.BaseRule;
import net.redborder.samza.rules.Rule;

import java.util.Map;

public class MemoryRule extends Rule {
    BaseRule rule;
    String memoryField;
    Object memory;

    public MemoryRule(String ruleUuid, BaseRule rule, String memoryField) {
        super(ruleUuid);
        this.rule = rule;
        this.memoryField = memoryField;
    }

    @Override
    public Boolean disable(String endpoint, Map<String, Object> condition) {
        return rule.verify(endpoint, condition);
    }

    @Override
    public Boolean enable(String endpoint, Map<String, Object> condition) {
        Boolean enabled = rule.verify(endpoint, condition);

        if(enabled){
            memory = condition.get(memoryField);
        }

        return enabled;
    }

    public Object getLastMemory(){
        return memory;
    }
}
