package net.redborder.samza.util;

import java.io.IOException;
import java.util.*;

import com.aerospike.client.*;
import com.aerospike.client.async.AsyncClient;
import com.aerospike.client.async.AsyncClientPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

import org.apache.samza.config.Config;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AerospikeStore {
    private static final Logger log = LoggerFactory.getLogger(AerospikeStore.class);

    AsyncClient client;
    List<String> hosts;
    Integer timeout;
    WritePolicy writePolicy;
    ObjectMapper objectMapper = new ObjectMapper();

    public AerospikeStore(Config config) {
        hosts = config.getList("redborder.stores.extension.aerospike.servers");
        timeout = config.getInt("redborder.stores.extension.aerospike.clientTimeout", 500);
        writePolicy = new WritePolicy();
        writePolicy.recordExistsAction = RecordExistsAction.UPDATE;

        List<Host> servers = new ArrayList<>();

        for (String host : hosts) {
            String[] sp = host.split(":");
            servers.add(new Host(sp[0], Integer.parseInt(sp[1])));
        }

        AsyncClientPolicy cPolicy = new AsyncClientPolicy();
        cPolicy.timeout = timeout;

        client = new AsyncClient(cPolicy, servers.toArray(new Host[servers.size()]));
    }

    public Map<String, Object> get(String namespace, String collection, String key) {
        Map<String, Object> result = new HashMap<>();
        try {
            Record record = client.get(new Policy(), new Key(namespace, collection, key));


            if (record != null) {
                result.putAll(record.bins);
            }

        } catch (AerospikeException ex) {
            log.error("Aerospike exception", ex);
        }
        return result;

    }

    public Boolean exist(String namespace, String collection, String key) {
        Boolean exist = false;
        try {
            exist = client.exists(new Policy(), new Key(namespace, collection, key));
        } catch (AerospikeException ex) {
            log.error("Aerospike exception", ex);
        }

        return exist;
    }

    public void put(String namespace, String collection, String key, Set<String> columns, Collection<Object> values) {
        try {
            List<String> columnsList = new ArrayList<>();
            columnsList.addAll(columns);

            List<Object> valueList = new ArrayList<>();
            valueList.addAll(values);

            Key asKey = new Key(namespace, collection, key);
            Bin[] bins = new Bin[columnsList.size()];

            WritePolicy policy = new WritePolicy();
            policy.recordExistsAction = RecordExistsAction.UPDATE;

            for (int i = 0; i < columns.size(); i++) {
                bins[i] = new Bin(columnsList.get(i), valueList.get(i));
            }

            client.put(policy, asKey, bins);
        } catch (AerospikeException ex) {
            log.error("Aerospike exception", ex);
        }
    }

    public void remove(String namespace, String collection, String key) {
        try {
            Key asKey = new Key(namespace, collection, key);
            client.delete(new WritePolicy(), asKey);
        } catch (AerospikeException ex) {
            log.error("Aerospike exception", ex);
        }
    }

    public void increment(String namespace, String collection, String key, Set<String> columns, Collection<Integer> values) {
        try {
            List<String> columnsList = new ArrayList<>();
            columnsList.addAll(columns);

            List<Object> valueList = new ArrayList<>();
            valueList.addAll(values);

            Key asKey = new Key(namespace, collection, key);
            Operation[] operations = new Operation[columnsList.size()];

            WritePolicy policy = new WritePolicy();
            policy.recordExistsAction = RecordExistsAction.UPDATE;

            for (int i = 0; i < columnsList.size(); i++) {
                operations[i] = Operation.add(new Bin(columnsList.get(i), valueList.get(i)));
            }

            client.operate(policy, asKey, operations);
        } catch (AerospikeException ex) {
            log.error("Aerospike exception", ex);
        }
    }

    public AsyncClient getClient() {
        return client;
    }

    public void updateIOC(String endpointUUID, List<String> iocs) {
        Set<String> iocBinKeys = new LinkedHashSet<>();
        iocBinKeys.add("endpoint_uuid");
        iocBinKeys.add("ioc");
        iocBinKeys.add("num_ioc");

        try {
            String iocStr = objectMapper.writeValueAsString(iocs);

            if (iocs.size() == 0) {
                remove("malware", "ioc", endpointUUID);
            } else {
                if (exist("malware", "ioc", endpointUUID)) {
                    put("malware", "ioc", endpointUUID,
                            iocBinKeys, Arrays.<Object>asList(endpointUUID, iocStr, iocs.size()));
                } else {
                    iocBinKeys.add("compromised");

                    put("malware", "ioc", endpointUUID,
                            iocBinKeys,
                            Arrays.<Object>asList(endpointUUID, iocStr, iocs.size(), System.currentTimeMillis() / 1000));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
