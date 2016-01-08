package net.redborder.samza.rules.logic;

import net.redborder.samza.rules.BaseRule;
import net.redborder.samza.rules.Rule;

import java.util.List;
import java.util.Map;

public class ANDRule extends BaseRule {
    List<Rule> rules;

    public ANDRule(String ruleUuid, List<Rule> rules) {
        super(ruleUuid);
        this.rules = rules;
        type = "and";
    }

    @Override
    public Boolean disable(String endpoint, Map<String, Object> condition) {
        Boolean isEnabled = false;

        for(Rule rule : rules){
            Boolean verification = rule.verify(endpoint, condition);
            if(verification != null && !verification){
                isEnabled = true;
                break;
            }
        }

        return isEnabled;
    }

    @Override
    public Boolean enable(String endpoint, Map<String, Object> condition) {
        Boolean isEnabled = true;

        for(Rule rule : rules){
            Boolean verification = rule.verify(endpoint, condition);
            if(verification == null || !verification){
                isEnabled = false;
                break;
            }
        }

        return isEnabled;
    }

    public String getType(){
        return type;
    }
}
