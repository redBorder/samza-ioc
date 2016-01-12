package net.redborder.samza.rules.special;

import junit.framework.TestCase;
import net.redborder.samza.rules.Rule;
import net.redborder.samza.rules.base.ContainsRule;
import net.redborder.samza.rules.base.FewConditionsException;
import net.redborder.samza.rules.base.FieldRule;
import net.redborder.samza.rules.logic.ORRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;


@RunWith(MockitoJUnitRunner.class)
public class ConnectionRuleTest extends TestCase {

    @Test
    public void checkIPenable() throws FewConditionsException {
        ConnectionRule connectionRule = new ConnectionRule("CONNECTION", ";", ":", "connections",
                Arrays.asList("192.168.1.1"), Collections.EMPTY_LIST, Collections.EMPTY_LIST);

        Map<String, Object> message = new HashMap<>();
        message.put("connections", "192.168.1.1:84112;192.168.2.2:12341");
        assertTrue(connectionRule.verify("AABBCCDD", message));
    }

    @Test
    public void checkPortenable() throws FewConditionsException {
        ConnectionRule connectionRule = new ConnectionRule("CONNECTION", ";", ":", "connections",
                Collections.EMPTY_LIST, Arrays.asList("12341"), Collections.EMPTY_LIST);

        Map<String, Object> message = new HashMap<>();
        message.put("connections", "192.168.1.1:84112;192.168.2.2:12341");
        assertTrue(connectionRule.verify("AABBCCDD", message));
    }

    @Test
    public void checkConnectionenable() throws FewConditionsException {
        ConnectionRule connectionRule = new ConnectionRule("CONNECTION", ";", ":", "connections",
                Collections.EMPTY_LIST, Collections.EMPTY_LIST, Arrays.asList("192.168.1.1:84112"));

        Map<String, Object> message = new HashMap<>();
        message.put("connections", "192.168.1.1:84112;192.168.2.2:12341");
        assertTrue(connectionRule.verify("AABBCCDD", message));
    }

    @Test(expected = FewConditionsException.class)
    public void fewConditionExceptionTest() throws FewConditionsException {
        ConnectionRule connectionRule = new ConnectionRule("CONNECTION", ";", ":", "connections",
                Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }
}
