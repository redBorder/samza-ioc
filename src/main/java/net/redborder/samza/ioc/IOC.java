package net.redborder.samza.ioc;

import java.util.Set;

public class IOC {
    public String iocUuid;
    public Set<String> rules;

    public IOC(String iocUuid){
        this.iocUuid = iocUuid;
    }

    public Boolean verify(Set<String> enabledRules){
        boolean enabled = true;

        for(String rule : rules){
            if(!enabledRules.contains(rule)){
                enabled = false;
            }
        }

        return enabled;
    }
}
