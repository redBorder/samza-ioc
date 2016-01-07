package net.redborder.samza.rules;

import net.redborder.samza.rules.base.ContainsRule;
import net.redborder.samza.rules.base.FieldRule;
import net.redborder.samza.rules.logic.ANDRule;
import net.redborder.samza.rules.logic.ORRule;
import net.redborder.samza.rules.memory.MemoryRule;
import net.redborder.samza.rules.memory.MemoryRuleManager;
import net.redborder.samza.rules.memory.QueryMemoryRule;
import org.apache.samza.config.Config;
import org.apache.samza.storage.kv.KeyValueIterator;
import org.apache.samza.storage.kv.KeyValueStore;
import org.apache.samza.task.TaskContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class RulesManager {
    MemoryRuleManager memoryRuleManager;
    List<Rule> rules = new LinkedList<>();
    KeyValueStore<String, Set<String>> rulesStores;

    private static final Logger log = LoggerFactory.getLogger(RulesManager.class);


    public RulesManager(Config config, TaskContext taskContext) {
        String jsonRules = config.get("net.redborder.ioc.rules", "{}");
        String jsonMemoryRules = config.get("net.redborder.ioc.rules.memory", "{}");

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Map<String, Object>> memoryRules = objectMapper.readValue(jsonMemoryRules, List.class);
            initMemoryRules(memoryRules, taskContext);
        } catch (IOException e) {
            log.error("Error parser memory rules", e);
        }

        try {
            List<Map<String, Object>> rules = objectMapper.readValue(jsonRules, List.class);
            initRules(rules, taskContext);
        } catch (IOException e) {
            log.error("Error parser  rules", e);
        }
    }

    private void initMemoryRules(List<Map<String, Object>> rules, TaskContext taskContext) {
        List<MemoryRule> memoryRules = new LinkedList<>();

        for (Map<String, Object> rule : rules) {
            String uuid = (String) rule.get("uuid");
            String memoryField = (String) rule.get("memoryField");
            Map<String, Object> baseRuleMap = (Map<String, Object>) rule.get("baseRule");

            BaseRule baseRule = RuleBuilder.buildBaseRule(memoryRuleManager, baseRuleMap);
            memoryRules.add(new MemoryRule(uuid, baseRule, memoryField));
        }

        this.memoryRuleManager =
                new MemoryRuleManager(memoryRules, (KeyValueStore<String, Set<String>>) taskContext.getStore("memoryRules"));
    }

    private void initRules(List<Map<String, Object>> mapRules, TaskContext taskContext) {
        this.rulesStores = (KeyValueStore<String, Set<String>>) taskContext.getStore("rules");

        for (Map<String, Object> mapRule : mapRules) {
            Rule rule = RuleBuilder.buildRule(memoryRuleManager, mapRule);
            rules.add(rule);
        }
    }

    public void verify(String endpoint, Map<String, Object> condition) {
        Set<String> endpointRules = rulesStores.get(endpoint);
        memoryRuleManager.verify(endpoint, condition);

        if (endpointRules == null) {
            endpointRules = new HashSet<>();
        }

        for (Rule rule : rules) {
            Boolean verification = rule.verify(endpoint, condition);
            if(verification != null) {
                if (verification) {
                    endpointRules.add(rule.ruleUuid);
                } else {
                    endpointRules.remove(rule.ruleUuid);
                }
            }
        }


        rulesStores.put(endpoint, endpointRules);
    }

    public KeyValueIterator<String, Set<String>> getRules() {
        return rulesStores.all();
    }

    public static class RuleBuilder {

        public static BaseRule buildBaseRule(MemoryRuleManager memoryRuleManager, Map<String, Object> ruleMap) {
            String type = (String) ruleMap.get("type");
            String uuid = (String) ruleMap.get("uuid");
            BaseRule baseRule = null;

            if (type.equals("field")) {
                Map<String, Object> enabledConditions = (Map<String, Object>) ruleMap.get("enabledConditions");
                Map<String, Object> disabledConditions = (Map<String, Object>) ruleMap.get("disabledConditions");
                baseRule = new FieldRule(uuid, enabledConditions, disabledConditions);

            } else if (type.equals("contains")) {
                Map<String, String> enabledConditions = (Map<String, String>) ruleMap.get("enabledConditions");
                Map<String, String> disabledConditions = (Map<String, String>) ruleMap.get("disabledConditions");
                baseRule = new ContainsRule(uuid, enabledConditions, disabledConditions);

            } else if (type.equals("and")) {
                List<Rule> rules = new LinkedList<>();
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>) ruleMap.get("rules");

                for (Map<String, Object> rMap : rulesMap) {
                    Rule r = buildRule(memoryRuleManager, rMap);
                    rules.add(r);
                }

                baseRule = new ANDRule(uuid, rules);
            } else if (type.equals("or")) {
                List<Rule> rules = new LinkedList<>();
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>) ruleMap.get("rules");

                for (Map<String, Object> rMap : rulesMap) {
                    Rule r = buildRule(memoryRuleManager, rMap);
                    rules.add(r);
                }

                baseRule = new ORRule(uuid, rules);
            } else {
                log.warn("The type {} isn't a valid base rule type.", type);
            }

            return baseRule;
        }

        public static Rule buildRule(MemoryRuleManager memoryRuleManager, Map<String, Object> ruleMap) {
            String type = (String) ruleMap.get("type");
            String uuid = (String) ruleMap.get("uuid");
            Rule rule;

            if (type.equals("queryMemory")) {
                rule = new QueryMemoryRule(uuid, memoryRuleManager);
            } else {
                rule = buildBaseRule(memoryRuleManager, ruleMap);
            }

            return rule;
        }
    }
}