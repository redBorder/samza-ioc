package net.redborder.samza.rules.base;

import net.redborder.samza.rules.BaseRule;

import java.util.Map;

public class FieldRule extends BaseRule {
    Map<String, Object> enableConditions;
    Map<String, Object> disableConditions;

    public FieldRule(String ruleUuid, Map<String, Object> enableConditions, Map<String, Object> disableConditions) {
        super(ruleUuid);
        this.enableConditions = enableConditions;
        this.disableConditions = disableConditions;
    }

    @Override
    public Boolean disable(String endpoint, Map<String, Object> condition) {
        Boolean enabled = true;

        for (Map.Entry<String, Object> entry : disableConditions.entrySet()) {
            if (!condition.containsKey(entry.getKey()) || !condition.get(entry.getKey()).equals(entry.getValue())) {
                enabled = false;
                break;
            }
        }

        return enabled;
    }

    @Override
    public Boolean enable(String endpoint, Map<String, Object> condition) {
        Boolean enabled = true;

        for (Map.Entry<String, Object> entry : enableConditions.entrySet()) {
            if (!condition.containsKey(entry.getKey()) || !condition.get(entry.getKey()).equals(entry.getValue())) {
                enabled = false;
                break;
            }
        }

        return enabled;
    }
}
