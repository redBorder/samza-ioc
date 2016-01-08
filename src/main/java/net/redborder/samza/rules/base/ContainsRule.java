package net.redborder.samza.rules.base;

import net.redborder.samza.rules.BaseRule;

import java.util.Map;

public class ContainsRule extends BaseRule {
    Map<String, String> enableConditions;
    Map<String, String> disableConditions;

    public ContainsRule(String ruleUuid, Map<String, String> enableConditions, Map<String, String> disableConditions) throws FewConditionsException {
        super(ruleUuid);

        if(enableConditions == null || disableConditions == null || (enableConditions.size() + disableConditions.size()) < 2){
            throw new FewConditionsException("You must provide al least one enable and disable conditions.");
        }

        this.enableConditions = enableConditions;
        this.disableConditions = disableConditions;
        type = "contains";
    }

    @Override
    public Boolean disable(String endpoint, Map<String, Object> condition) {
        Boolean enabled = true;

        for (Map.Entry<String, String> entry : disableConditions.entrySet()) {
            if (!condition.containsKey(entry.getKey()) || !condition.get(entry.getKey()).toString().contains(entry.getValue())) {
                enabled = false;
                break;
            }
        }


        return enabled;
    }

    @Override
    public Boolean enable(String endpoint, Map<String, Object> condition) {
        Boolean enabled = true;

        for (Map.Entry<String, String> entry : enableConditions.entrySet()) {
            if (!condition.containsKey(entry.getKey()) || !condition.get(entry.getKey()).toString().contains(entry.getValue())) {
                enabled = false;
                break;
            }
        }


        return enabled;
    }
}
