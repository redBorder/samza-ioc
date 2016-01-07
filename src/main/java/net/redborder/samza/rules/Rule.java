package net.redborder.samza.rules;

import java.util.Map;

public abstract class Rule {
    public String ruleUuid;


    public Rule(String ruleUuid) {
        this.ruleUuid = ruleUuid;
    }

    public Boolean verify(String endpoint, Map<String, Object> condition){
        Boolean isEnabled = null;

        if(enable(endpoint, condition)){
            isEnabled = true;
        }else if(disable(endpoint, condition)){
            isEnabled = false;
        }

        System.out.println("["+ruleUuid+"]" + " ENABLED: " + (enable(endpoint, condition)));
        System.out.println("["+ruleUuid+"]" + " DISABLED: " + (disable(endpoint, condition)));
        System.out.println("["+ruleUuid+"]" + " RESULT: " + isEnabled);
        System.out.println("-------------- --------------- -----------");

        return isEnabled;
    }

    public abstract Boolean enable(String endpoint, Map<String, Object> condition);
    public abstract Boolean disable(String endpoint, Map<String, Object> condition);
}
