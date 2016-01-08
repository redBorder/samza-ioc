package net.redborder.samza.rules.memory;

import junit.framework.TestCase;

import net.redborder.samza.rules.base.FewConditionsException;
import net.redborder.samza.rules.base.FieldRule;
import net.redborder.samza.util.MockKeyValueStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.*;


@RunWith(MockitoJUnitRunner.class)
public class MemoryRuleManagerTest extends TestCase{

    @Test
    public void onceMemoryRule() throws IOException, FewConditionsException {
        MockKeyValueStore memoryStore = new MockKeyValueStore();


        Map<String, Object> enabledConditions = new HashMap<>();
        Map<String, Object> disabledConditions = new HashMap<>();
        enabledConditions.put("field1", "value1");
        disabledConditions.put("field2", "value2");

        FieldRule fieldRule = new FieldRule("1234", enabledConditions, disabledConditions);

        List<MemoryRule> memoryRules = new ArrayList<>();
        memoryRules.add(new MemoryRule("memory1", fieldRule,"memoryField1"));

        MemoryRuleManager memoryRuleManager = new MemoryRuleManager(memoryRules, memoryStore);

        Map<String, Object> message = new HashMap<>();
        message.put("field1", "value1");
        message.put("memoryField1", "mValue1");

        memoryRuleManager.verify("ABXYZ", message);

        assertTrue(memoryRuleManager.queryRule("ABXYZ", "memory1"));
    }

    @Test
    public void twoEnableTwoDisable() throws IOException, FewConditionsException{
        MockKeyValueStore memoryStore = new MockKeyValueStore();


        Map<String, Object> enabledConditions = new HashMap<>();
        Map<String, Object> disabledConditions = new HashMap<>();
        enabledConditions.put("field1", "value1");
        disabledConditions.put("field2", "value2");

        FieldRule fieldRule = new FieldRule("FIELD", enabledConditions, disabledConditions);

        List<MemoryRule> memoryRules = new ArrayList<>();
        memoryRules.add(new MemoryRule("memory1", fieldRule,"memoryField1"));

        MemoryRuleManager memoryRuleManager = new MemoryRuleManager(memoryRules, memoryStore);

        Map<String, Object> messageEnable = new HashMap<>();
        messageEnable.put("field1", "value1");
        messageEnable.put("memoryField1", "mValue1");

        memoryRuleManager.verify("ABXYZ", messageEnable);

        assertTrue(memoryRuleManager.queryRule("ABXYZ", "memory1"));

        messageEnable.put("field1", "value1");
        messageEnable.put("memoryField1", "mValue2");

        memoryRuleManager.verify("ABXYZ", messageEnable);

        assertTrue(memoryRuleManager.queryRule("ABXYZ", "memory1"));

        Map<String, Object> messageDisable = new HashMap<>();
        messageDisable.put("field2", "value2");
        messageDisable.put("memoryField1", "mValue2");

        memoryRuleManager.verify("ABXYZ", messageDisable);

        assertTrue(memoryRuleManager.queryRule("ABXYZ", "memory1"));

        messageDisable.put("field2", "value2");
        messageDisable.put("memoryField1", "mValue1");

        memoryRuleManager.verify("ABXYZ", messageDisable);

        assertFalse(memoryRuleManager.queryRule("ABXYZ", "memory1"));
    }

    @Test
    public void queryMemoryRule() throws IOException, InvalidMemoryRuleException, FewConditionsException {
        MockKeyValueStore memoryStore = new MockKeyValueStore();


        Map<String, Object> enabledConditions = new HashMap<>();
        Map<String, Object> disabledConditions = new HashMap<>();
        enabledConditions.put("field1", "value1");
        disabledConditions.put("field2", "value2");

        FieldRule fieldRule = new FieldRule("1234", enabledConditions, disabledConditions);

        List<MemoryRule> memoryRules = new ArrayList<>();
        memoryRules.add(new MemoryRule("memory1", fieldRule,"memoryField1"));

        MemoryRuleManager memoryRuleManager = new MemoryRuleManager(memoryRules, memoryStore);

        Map<String, Object> message = new HashMap<>();
        message.put("field1", "value1");
        message.put("memoryField1", "mValue1");

        QueryMemoryRule queryMemoryRule = new QueryMemoryRule("memory1", memoryRuleManager);

        assertFalse(queryMemoryRule.verify("ABXYZ", message));
        memoryRuleManager.verify("ABXYZ", message);
        assertTrue(queryMemoryRule.verify("ABXYZ", message));
    }

    @Test
    public void isValidQuery() throws IOException, InvalidMemoryRuleException, FewConditionsException {
        MockKeyValueStore memoryStore = new MockKeyValueStore();

        Map<String, Object> enabledConditions = new HashMap<>();
        Map<String, Object> disabledConditions = new HashMap<>();
        enabledConditions.put("field1", "value1");
        disabledConditions.put("field2", "value2");

        FieldRule fieldRule = new FieldRule("1234", enabledConditions, disabledConditions);

        List<MemoryRule> memoryRules = new ArrayList<>();
        memoryRules.add(new MemoryRule("memory1", fieldRule,"memoryField1"));

        MemoryRuleManager memoryRuleManager = new MemoryRuleManager(memoryRules, memoryStore);

        assertTrue(memoryRuleManager.isValidRule("memory1"));
    }

    @Test(expected=InvalidMemoryRuleException.class)
    public void memoryRuleException() throws IOException, InvalidMemoryRuleException, FewConditionsException {
        MockKeyValueStore memoryStore = new MockKeyValueStore();

        Map<String, Object> enabledConditions = new HashMap<>();
        Map<String, Object> disabledConditions = new HashMap<>();
        enabledConditions.put("field1", "value1");
        disabledConditions.put("field2", "value2");

        FieldRule fieldRule = new FieldRule("1234", enabledConditions, disabledConditions);

        List<MemoryRule> memoryRules = new ArrayList<>();
        memoryRules.add(new MemoryRule("memory1", fieldRule,"memoryField1"));

        MemoryRuleManager memoryRuleManager = new MemoryRuleManager(memoryRules, memoryStore);

        memoryRuleManager.isValidRule("memory2");
    }
}
