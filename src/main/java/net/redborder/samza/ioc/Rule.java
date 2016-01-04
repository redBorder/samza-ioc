package net.redborder.samza.ioc;

import java.util.HashMap;
import java.util.Map;

public class Rule {

    public String conditionEnable;
    public String conditionDisable;

    public Integer ruleUuid;

    public Rule(String conditionEnable, String conditionDisable, Integer ruleUuid) {
        this.conditionEnable = conditionEnable;
        this.conditionDisable = conditionDisable;
        this.ruleUuid = ruleUuid;
    }

    public Boolean verify(String condition) {
        boolean isEnabled = false;

        if (condition.contains(conditionEnable)) {
            isEnabled = true;
        } else if (condition.contains(conditionDisable)) {
            isEnabled = false;
        }

        return isEnabled;
    }
}
