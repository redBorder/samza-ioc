package net.redborder.samza.rules.special;

import net.redborder.samza.rules.Rule;
import net.redborder.samza.rules.base.FewConditionsException;

import java.util.List;
import java.util.Map;

public class ConnectionRule extends Rule {
    List<String> ips;
    List<String> ports;
    List<String> connections;
    String connectionsField;
    String connectionSplit;
    String portSplit;

    public ConnectionRule(String ruleUuid, String connectionSplit, String portSplit, String connectionsField, List<String> ips, List<String> ports,
                          List<String> connections) throws FewConditionsException {
        super(ruleUuid);
        type = "connection";

        this.connectionsField = connectionsField;
        this.connectionSplit = connectionSplit;
        this.portSplit = portSplit;
        this.ips = ips;
        this.ports = ports;
        this.connections = connections;

        if(ips.size() < 1 && ports.size() < 1 && connections.size() < 1){
            throw new FewConditionsException("You must provide al least one condition [ips, ports or connections]");
        }
    }

    @Override
    public Boolean enable(String endpoint, Map<String, Object> condition) {
        Boolean enable = false;

        if(condition.get("type") != null && condition.get("type").equals("connection")) {
            String connectionsStr = (String) condition.get(connectionsField);

            if (connectionsStr != null) {
                String[] connnections = connectionsStr.split(connectionSplit);

                for (String connection : connnections) {
                    if (connections.contains(connection)) {
                        enable = true;
                        break;
                    }

                    String[] hostPort = connection.split(portSplit);

                    if (hostPort.length == 2 && (ips.contains(hostPort[0]) || ports.contains(hostPort[1]))) {
                        enable = true;
                        break;
                    }
                }
            }
        }
        return enable;
    }

    @Override
    public Boolean disable(String endpoint, Map<String, Object> condition) {
        return false;
    }
}
