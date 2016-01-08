package net.redborder.samza.rules.memory;

import org.apache.samza.storage.kv.KeyValueStore;

import java.util.*;

public class MemoryRuleManager {
    public List<MemoryRule> memoryRules = new LinkedList<>();
    public List<String> memoryRulesIds = new LinkedList<>();

    KeyValueStore<String, Set<String>> store;

    public MemoryRuleManager(List<MemoryRule> memoryRules, KeyValueStore<String, Set<String>> store) {
        this.memoryRules = memoryRules;

        for(MemoryRule memoryRule : memoryRules){
            memoryRulesIds.add(memoryRule.ruleUuid);
        }

        this.store = store;
    }

    public void verify(String endpoint, Map<String, Object> condition) {
        Set<String> enabledRules = store.get(endpoint);

        if(enabledRules == null){
            enabledRules = new HashSet<>();
        }

        for (MemoryRule rule : memoryRules) {
            Boolean verification = rule.verify(endpoint, condition);
            if(verification != null) {
                if (verification) {
                    enabledRules.add(rule.ruleUuid + ":" + rule.getLastMemory());
                    store.put(endpoint, enabledRules);
                } else {
                    enabledRules.remove(rule.ruleUuid + ":" + rule.getLastMemory());
                    store.put(endpoint, enabledRules);
                }
            }
        }
    }

    public Boolean isValidRule(String uuid) throws InvalidMemoryRuleException {
        if(!memoryRulesIds.contains(uuid)){
            throw new InvalidMemoryRuleException("The memory rule [" + uuid + "], doesn't exist. " +
                    "Valid memory rules " + memoryRulesIds);
        }

        return true;
    }

    public Boolean queryRule(String endpoint, String uuid) {
        Boolean enabled = false;

        Set<String> rules = store.get(endpoint);
        if(rules != null) {
            for (String rule : rules) {
                if (rule.split(":")[0].equals(uuid)) {
                    enabled = true;
                    break;
                }
            }
        }

        return enabled;
    }

}
