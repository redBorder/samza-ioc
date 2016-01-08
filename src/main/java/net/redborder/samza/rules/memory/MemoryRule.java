package net.redborder.samza.rules.memory;


import net.redborder.samza.rules.BaseRule;
import net.redborder.samza.rules.Rule;

import java.util.Map;

public class MemoryRule extends Rule {
    public BaseRule rule;
    public String memoryField;
    Object memory;

    public MemoryRule(String ruleUuid, BaseRule rule, String memoryField) {
        super(ruleUuid);
        this.rule = rule;
        this.memoryField = memoryField;
        type = "memory";
    }

    @Override
    public Boolean disable(String endpoint, Map<String, Object> condition) {
        Boolean enabled = rule.verify(endpoint, condition);

        if(enabled != null && !enabled){
            memory = condition.get(memoryField);
        }

        if(enabled == null) enabled = false;
        return !enabled;
    }

    @Override
    public Boolean enable(String endpoint, Map<String, Object> condition) {
        Boolean enabled = rule.verify(endpoint, condition);

        if(enabled != null && enabled){
            memory = condition.get(memoryField);
        }

        if(enabled == null) enabled = false;
        return enabled;
    }

    public Object getLastMemory(){
        return memory;
    }
}
