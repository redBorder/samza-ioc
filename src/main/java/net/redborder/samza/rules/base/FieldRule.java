package net.redborder.samza.rules.base;

import net.redborder.samza.rules.BaseRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class FieldRule extends BaseRule {
    private static final Logger log = LoggerFactory.getLogger(FieldRule.class);
    Map<String, Object> enableConditions;
    Map<String, Object> disableConditions;

    public FieldRule(String ruleUuid, Map<String, Object> enableConditions, Map<String, Object> disableConditions) throws FewConditionsException {
        super(ruleUuid);

        if (enableConditions == null || enableConditions.size() < 1) {
            throw new FewConditionsException("You must provide al least one enable conditions.\n   " +
                    "* enable: " + enableConditions + "\n   * disable: " + disableConditions);
        }

        this.enableConditions = enableConditions;
        this.disableConditions = disableConditions;

        type = "field";
    }

    @Override
    public Boolean disable(String endpoint, Map<String, Object> condition) {
        Boolean enabled = true;

        if (disableConditions == null || disableConditions.isEmpty()) {
            enabled = false;
        } else {
            for (Map.Entry<String, Object> entry : disableConditions.entrySet()) {
                if (!condition.containsKey(entry.getKey()) || !condition.get(entry.getKey()).equals(entry.getValue())) {
                    enabled = false;
                    break;
                }
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
