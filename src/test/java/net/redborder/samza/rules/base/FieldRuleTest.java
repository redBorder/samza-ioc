package net.redborder.samza.rules.base;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class FieldRuleTest extends TestCase{

    @Test
    public void enableRule() throws FewConditionsException {
        Map<String, Object> enabledConditions = new HashMap<>();
        Map<String, Object> disabledConditions = new HashMap<>();

        enabledConditions.put("field1", "value1");
        enabledConditions.put("field2", "value2");
        enabledConditions.put("field3", "value3");

        disabledConditions.put("field4", "value4");
        disabledConditions.put("field5", "value5");
        disabledConditions.put("field6", "value6");

        FieldRule rule = new FieldRule("1234", enabledConditions, disabledConditions);

        Map<String, Object> message = new HashMap<>();
        message.put("field1", "value1");
        message.put("field2", "value2");
        message.put("field3", "value3");
        message.put("otherField1", "otherValue1");
        message.put("otherField2", "otherValue2");

        assertTrue(rule.verify("AABBCCDD", message));
    }

    @Test
    public void disableRule() throws FewConditionsException {
        Map<String, Object> enabledConditions = new HashMap<>();
        Map<String, Object> disabledConditions = new HashMap<>();

        enabledConditions.put("field1", "value1");
        enabledConditions.put("field2", "value2");
        enabledConditions.put("field3", "value3");

        disabledConditions.put("field4", "value4");
        disabledConditions.put("field5", "value5");
        disabledConditions.put("field6", "value6");

        FieldRule rule = new FieldRule("1234", enabledConditions, disabledConditions);

        Map<String, Object> message = new HashMap<>();
        message.put("field4", "value4");
        message.put("field5", "value5");
        message.put("field6", "value6");
        message.put("otherField1", "otherValue1");
        message.put("otherField2", "otherValue2");

        assertFalse(rule.verify("AABBCCDD", message));
    }

    @Test
    public void notEnableAndDisableRule() throws FewConditionsException {
        Map<String, Object> enabledConditions = new HashMap<>();
        Map<String, Object> disabledConditions = new HashMap<>();

        enabledConditions.put("field1", "value1");
        enabledConditions.put("field2", "value1");
        enabledConditions.put("field3", "value3");

        disabledConditions.put("field4", "value4");
        disabledConditions.put("field5", "value5");
        disabledConditions.put("field6", "value6");

        FieldRule rule = new FieldRule("1234", enabledConditions, disabledConditions);

        Map<String, Object> message = new HashMap<>();
        message.put("field1", "value4");
        message.put("field2", "value5");
        message.put("field3", "value7");
        message.put("otherField1", "otherValue1");
        message.put("otherField2", "otherValue2");

        assertNull(rule.verify("AABBCCDD", message));
    }

    @Test
    public void enableAndDisablerule() throws FewConditionsException {
        Map<String, Object> enabledConditions = new HashMap<>();
        Map<String, Object> disabledConditions = new HashMap<>();

        enabledConditions.put("field1", "value1");
        enabledConditions.put("field2", "value2");
        enabledConditions.put("field3", "value3");

        disabledConditions.put("field4", "value4");
        disabledConditions.put("field5", "value5");
        disabledConditions.put("field6", "value6");

        FieldRule rule = new FieldRule("1234", enabledConditions, disabledConditions);

        Map<String, Object> messageEnable = new HashMap<>();
        messageEnable.put("field1", "value1");
        messageEnable.put("field2", "value2");
        messageEnable.put("field3", "value3");
        messageEnable.put("otherField1", "otherValue1");
        messageEnable.put("otherField2", "otherValue2");

        assertTrue(rule.verify("AABBCCDD", messageEnable));

        Map<String, Object> messageDisable = new HashMap<>();
        messageDisable.put("field4", "value4");
        messageDisable.put("field5", "value5");
        messageDisable.put("field6", "value6");
        messageDisable.put("otherField1", "otherValue1");
        messageDisable.put("otherField2", "otherValue2");

        assertFalse(rule.verify("AABBCCDD", messageDisable));
    }

    @Test(expected = FewConditionsException.class)
    public void fewConditionExceptionTest() throws FewConditionsException {
        Map<String, Object> enabledConditions = new HashMap<>();
        Map<String, Object> disabledConditions = new HashMap<>();

        FieldRule rule = new FieldRule("1234", enabledConditions, disabledConditions);
    }
}
