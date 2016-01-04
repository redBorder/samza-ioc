package net.redborder.samza.ioc;

import java.util.Set;

public class IOC {
    public Integer iocUuid;
    public Set<Integer> rules;

    public IOC(Integer iocUuid){
        this.iocUuid = iocUuid;
    }

    public Boolean verify(Set<Integer> enabledRules){
        boolean enabled = true;

        for(Integer rule : rules){
            if(!enabledRules.contains(rule)){
                enabled = false;
            }
        }

        return enabled;
    }
}
