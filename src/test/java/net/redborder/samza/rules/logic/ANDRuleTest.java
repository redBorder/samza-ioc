package net.redborder.samza.rules.logic;

import junit.framework.TestCase;
import net.redborder.samza.rules.Rule;
import net.redborder.samza.rules.base.ContainsRule;
import net.redborder.samza.rules.base.FieldRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;


@RunWith(MockitoJUnitRunner.class)
public class ANDRuleTest extends TestCase {

    @Test
    public void allEnableRule() {
        Map<String, String> enabledConditionsContains = new HashMap<>();
        Map<String, String> disabledConditionsContains = new HashMap<>();
        enabledConditionsContains.put("field1", "value1");
        disabledConditionsContains.put("field2", "value2");

        Map<String, Object> enabledConditionsField = new HashMap<>();
        Map<String, Object> disabledConditionsField = new HashMap<>();
        enabledConditionsField.put("field3", "value3");
        enabledConditionsField.put("field4", "value4");

        ContainsRule containsRule = new ContainsRule("CONTAINS", enabledConditionsContains, disabledConditionsContains);
        FieldRule fieldRule = new FieldRule("FIELD", enabledConditionsField, disabledConditionsField);

        List<Rule> rules = new LinkedList<>();
        rules.add(containsRule);
        rules.add(fieldRule);

        ANDRule andRule = new ANDRule("AND", rules);

        Map<String, Object> message = new HashMap<>();
        message.put("field1", "value1co1fi1m1d");
        message.put("field2", "value2 d1d12d1");
        message.put("field3", "value3");
        message.put("field4", "value4");
        message.put("otherField1", "otherValue1");
        message.put("otherField2", "otherValue2");

        assertTrue(andRule.verify("AABBCCDD", message));
    }

    @Test
    public void onceEnabledRule() {
        Map<String, String> enabledConditionsContains = new HashMap<>();
        Map<String, String> disabledConditionsContains = new HashMap<>();
        enabledConditionsContains.put("field1", "value1");
        disabledConditionsContains.put("field2", "value2");

        Map<String, Object> enabledConditionsField = new HashMap<>();
        Map<String, Object> disabledConditionsField = new HashMap<>();
        enabledConditionsField.put("field7", "value3");
        enabledConditionsField.put("field8", "value4");

        ContainsRule containsRule = new ContainsRule("CONTAINS", enabledConditionsContains, disabledConditionsContains);
        FieldRule fieldRule = new FieldRule("FIELD", enabledConditionsField, disabledConditionsField);

        List<Rule> rules = new LinkedList<>();
        rules.add(containsRule);
        rules.add(fieldRule);

        ANDRule andRule = new ANDRule("AND", rules);

        Map<String, Object> message = new HashMap<>();
        message.put("field1", "value1co1fi1m1d");
        message.put("field2", "value2 d1d12d1");
        message.put("field3", "value3");
        message.put("field4", "value4");
        message.put("otherField1", "otherValue1");
        message.put("otherField2", "otherValue2");

        assertFalse(andRule.verify("AABBCCDD", message));
    }

    @Test
    public void allDisableRule() {
        Map<String, String> enabledConditionsContains = new HashMap<>();
        Map<String, String> disabledConditionsContains = new HashMap<>();
        enabledConditionsContains.put("field1", "value1");
        disabledConditionsContains.put("field2", "value2");

        Map<String, Object> enabledConditionsField = new HashMap<>();
        Map<String, Object> disabledConditionsField = new HashMap<>();
        enabledConditionsField.put("field1", "value3");
        enabledConditionsField.put("field4", "value4");

        ContainsRule containsRule = new ContainsRule("CONTAINS", enabledConditionsContains, disabledConditionsContains);
        FieldRule fieldRule = new FieldRule("FIELD", enabledConditionsField, disabledConditionsField);

        List<Rule> rules = new LinkedList<>();
        rules.add(containsRule);
        rules.add(fieldRule);

        ANDRule andRule = new ANDRule("AND", rules);

        Map<String, Object> message = new HashMap<>();
        message.put("field1", "value1co1fi1m1d");
        message.put("field2", "value2 d1d12d1");
        message.put("field3", "value3");
        message.put("field4", "value4");
        message.put("otherField1", "otherValue1");
        message.put("otherField2", "otherValue2");

        assertFalse(andRule.verify("AABBCCDD", message));
    }

    @Test
    public void notEnableAndDisableRule() {
        Map<String, String> enabledConditionsContains = new HashMap<>();
        Map<String, String> disabledConditionsContains = new HashMap<>();
        enabledConditionsContains.put("field1", "value1");
        disabledConditionsContains.put("field2", "value2");

        Map<String, Object> enabledConditionsField = new HashMap<>();
        Map<String, Object> disabledConditionsField = new HashMap<>();
        enabledConditionsField.put("field1", "value3");
        disabledConditionsField.put("field4", "value4");

        ContainsRule containsRule = new ContainsRule("CONTAINS", enabledConditionsContains, disabledConditionsContains);
        FieldRule fieldRule = new FieldRule("FIELD", enabledConditionsField, disabledConditionsField);

        List<Rule> rules = new LinkedList<>();
        rules.add(containsRule);
        rules.add(fieldRule);

        ANDRule andRule = new ANDRule("AND", rules);

        Map<String, Object> message = new HashMap<>();
        message.put("field6", "value1co1fi1m1d");
        message.put("field7", "value2 d1d12d1");
        message.put("field8", "value3");
        message.put("field9", "value4");
        message.put("otherField1", "otherValue1");
        message.put("otherField2", "otherValue2");

        assertNull(andRule.verify("AABBCCDD", message));
    }

    @Test
    public void enableAndDisablerule() {
        Map<String, String> enabledConditionsContains = new HashMap<>();
        Map<String, String> disabledConditionsContains = new HashMap<>();
        enabledConditionsContains.put("field1", "value1");
        disabledConditionsContains.put("field2", "value2");

        Map<String, Object> enabledConditionsField = new HashMap<>();
        Map<String, Object> disabledConditionsField = new HashMap<>();
        enabledConditionsField.put("field3", "value3");
        disabledConditionsField.put("field4", "value4");

        ContainsRule containsRule = new ContainsRule("CONTAINS", enabledConditionsContains, disabledConditionsContains);
        FieldRule fieldRule = new FieldRule("FIELD", enabledConditionsField, disabledConditionsField);

        List<Rule> rules = new LinkedList<>();
        rules.add(containsRule);
        rules.add(fieldRule);

        ANDRule andRule = new ANDRule("AND", rules);

        Map<String, Object> messageEnable = new HashMap<>();
        messageEnable.put("field1", "value1");
        messageEnable.put("field3", "value3");
        messageEnable.put("field8", "value3");
        messageEnable.put("field9", "value4");
        messageEnable.put("otherField1", "otherValue1");
        messageEnable.put("otherField2", "otherValue2");

        assertTrue(andRule.verify("AABBCCDD", messageEnable));

        Map<String, Object> messageDisable = new HashMap<>();
        messageDisable.put("field2", "value2");
        messageDisable.put("field4", "value4");
        messageDisable.put("field8", "value3");
        messageDisable.put("field9", "value4");
        messageDisable.put("otherField1", "otherValue1");
        messageDisable.put("otherField2", "otherValue2");

        assertFalse(andRule.verify("AABBCCDD", messageDisable));
    }
}
