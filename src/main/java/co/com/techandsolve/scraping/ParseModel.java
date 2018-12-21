package co.com.techandsolve.scraping;

import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class ParseModel {

    private JsonNode jsonNode;
    private String docType;
    private String docId;

    public ParseModel(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public void setDNI(String type, String id) {
        this.docType = type;
        this.docId = id;
    }

    public Model toParse(String nodeKey) {
        JsonNode node = jsonNode.get(nodeKey);

        Connection.Method method = Connection.Method
                .valueOf(node.get("method").asText().toUpperCase());
        String action = node.get("action").asText();
        String type = node.get("type").asText();
        Map<String, String> data = new HashMap<>();
        node.get("data").fields()
                .forEachRemaining(
                        stringJsonNodeEntry ->
                                data.put(stringJsonNodeEntry.getKey(),
                                        formatDNT(stringJsonNodeEntry.getValue().asText())
                                )
                );

        return new Model(type, action, method, data);
    }

    private String formatDNT(String value) {
        value = value.replace("{__TYPE_DOC__}", docType);
        value = value.replace("{__NUM_DOC__}", docId);
        return value;
    }

    public static class Model {
        private String type;
        private String action;
        private Connection.Method method;
        private Map<String, String> data;

        private Model(String type, String action, Connection.Method method, Map<String, String> data) {
            this.type = type;
            this.action = action;
            this.method = method;
            this.data = data;
        }

        public String getType() {
            return type;
        }

        public String getAction() {
            return action;
        }

        public Map<String, String> getData() {
            return data;
        }


        public Connection.Method getMethod() {
            return method;
        }
    }

}
