package net.redborder.samza.rules;

import junit.framework.TestCase;

import net.redborder.samza.rules.base.FieldRule;
import net.redborder.samza.util.MockKeyValueStore;
import net.redborder.samza.util.MockTaskContext;
import org.apache.samza.config.Config;
import org.apache.samza.config.JavaSystemConfig;
import org.apache.samza.config.factories.PropertiesConfigFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.net.URI;

@RunWith(MockitoJUnitRunner.class)
public class RuleManagerTest extends TestCase{

    @Test
    public void buildBaseFieldRule() throws IOException {
        MockTaskContext mockTaskContext = new MockTaskContext();
        PropertiesConfigFactory propertiesConfigFactory = new PropertiesConfigFactory();
        Config config = propertiesConfigFactory.getConfig(
                URI.create("src/test/resources/rules/fieldConfig.properties"));

        RulesManager rulesManager = new RulesManager(config, mockTaskContext);

        assertEquals("field", rulesManager.rules.get(0).getType());
    }

    @Test
    public void buildBaseContainsRule() throws IOException {
        MockTaskContext mockTaskContext = new MockTaskContext();
        PropertiesConfigFactory propertiesConfigFactory = new PropertiesConfigFactory();
        Config config = propertiesConfigFactory.getConfig(
                URI.create("src/test/resources/rules/containsConfig.properties"));

        RulesManager rulesManager = new RulesManager(config, mockTaskContext);

        assertEquals("contains", rulesManager.rules.get(0).getType());
    }

    @Test
    public void buildBaseAndRule() throws IOException {
        MockTaskContext mockTaskContext = new MockTaskContext();
        PropertiesConfigFactory propertiesConfigFactory = new PropertiesConfigFactory();
        Config config = propertiesConfigFactory.getConfig(
                URI.create("src/test/resources/rules/andConfig.properties"));

        RulesManager rulesManager = new RulesManager(config, mockTaskContext);

        assertEquals("and", rulesManager.rules.get(0).getType());
    }

    @Test
    public void buildBaseOrRule() throws IOException {
        MockTaskContext mockTaskContext = new MockTaskContext();
        PropertiesConfigFactory propertiesConfigFactory = new PropertiesConfigFactory();
        Config config = propertiesConfigFactory.getConfig(
                URI.create("src/test/resources/rules/orConfig.properties"));

        RulesManager rulesManager = new RulesManager(config, mockTaskContext);

        assertEquals("or", rulesManager.rules.get(0).getType());
    }

    @Test
    public void buildMemoryRule() throws IOException {
        MockTaskContext mockTaskContext = new MockTaskContext();
        PropertiesConfigFactory propertiesConfigFactory = new PropertiesConfigFactory();
        Config config = propertiesConfigFactory.getConfig(
                URI.create("src/test/resources/rules/memoryConfig.properties"));

        RulesManager rulesManager = new RulesManager(config, mockTaskContext);

        assertEquals("memory", rulesManager.memoryRuleManager.memoryRules.get(0).getType());
        assertEquals("field", rulesManager.memoryRuleManager.memoryRules.get(0).rule.getType());
        assertEquals("memoryField1", rulesManager.memoryRuleManager.memoryRules.get(0).memoryField);
    }

    @Test
    public void buildConnectionRule() throws IOException {
        MockTaskContext mockTaskContext = new MockTaskContext();
        PropertiesConfigFactory propertiesConfigFactory = new PropertiesConfigFactory();

        Config config = propertiesConfigFactory.getConfig(
                URI.create("src/test/resources/rules/connectionsConfig.properties"));

        RulesManager rulesManager = new RulesManager(config, mockTaskContext);

        assertEquals("connection", rulesManager.rules.get(0).getType());
    }

    @Test
    public void buildQueryMemoryRule() throws IOException {
        MockTaskContext mockTaskContext = new MockTaskContext();
        PropertiesConfigFactory propertiesConfigFactory = new PropertiesConfigFactory();
        Config config = propertiesConfigFactory.getConfig(
                URI.create("src/test/resources/rules/queryMemoryConfig.properties"));

        RulesManager rulesManager = new RulesManager(config, mockTaskContext);

        assertEquals("memory", rulesManager.memoryRuleManager.memoryRules.get(0).getType());
        assertEquals("field", rulesManager.memoryRuleManager.memoryRules.get(0).rule.getType());
        assertEquals("memoryField1", rulesManager.memoryRuleManager.memoryRules.get(0).memoryField);

        assertEquals("queryMemory", rulesManager.rules.get(0).getType());
    }


    @Test
    public void invalidQueryMemoryRuleUUID() throws IOException {
        MockTaskContext mockTaskContext = new MockTaskContext();
        PropertiesConfigFactory propertiesConfigFactory = new PropertiesConfigFactory();
        Config config = propertiesConfigFactory.getConfig(
                URI.create("src/test/resources/rules/invalidQueryMemoryConfig.properties"));

        RulesManager rulesManager = new RulesManager(config, mockTaskContext);

        assertEquals("memory", rulesManager.memoryRuleManager.memoryRules.get(0).getType());
        assertEquals("field", rulesManager.memoryRuleManager.memoryRules.get(0).rule.getType());
        assertEquals("memoryField1", rulesManager.memoryRuleManager.memoryRules.get(0).memoryField);

        assertEquals(0, rulesManager.rules.size());
    }
}
