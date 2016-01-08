package net.redborder.samza.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class Rule {
    public String ruleUuid;
    private static final Logger log = LoggerFactory.getLogger(Rule.class);
    public String type;

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

        log.trace("[{}]" + " ENABLED: {}",ruleUuid, (enable(endpoint, condition)));
        log.trace("[{}]" + " DISABLED: {}",ruleUuid, (disable(endpoint, condition)));
        log.trace("[{}]" + " RESULT: {}",ruleUuid, isEnabled);

        return isEnabled;
    }

    public abstract Boolean enable(String endpoint, Map<String, Object> condition);
    public abstract Boolean disable(String endpoint, Map<String, Object> condition);

    public String getType(){
        return type;
    }
}
