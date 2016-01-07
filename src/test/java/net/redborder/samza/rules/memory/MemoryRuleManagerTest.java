package net.redborder.samza.rules.memory;

import junit.framework.TestCase;

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
    public void onceMemoryRule() throws IOException {
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
}
