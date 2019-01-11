package co.com.techandsolve.scraping.infra;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class ParseModel {

    private final JsonNode jsonNode;

    public ParseModel(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public static MetaModel getModel() {
        return new MetaModel(null, null, null);
    }


    public MetaModel toParse(String nodeKey) {
        JsonNode node = jsonNode.get(nodeKey);

        String method = getStringNode(node, "method");
        String path = getStringNode(node, "path");
        String query = getStringNode(node, "query");
        String action = getStringNode(node, "action");
        String type = getStringNode(node, "type");
        String selector = getStringNode(node, "selector");
        Map<String, String> data = new ConcurrentHashMap<>();
        Map<String, String> header = new ConcurrentHashMap<>();

        getMapNode(node, "data")
                .forEachRemaining(
                        stringJsonNodeEntry ->
                                data.put(
                                        stringJsonNodeEntry.getKey(),
                                        stringJsonNodeEntry.getValue().asText()
                                )
                );

        getMapNode(node, "header")
                .forEachRemaining(
                        stringJsonNodeEntry ->
                                header.put(
                                        stringJsonNodeEntry.getKey(),
                                        stringJsonNodeEntry.getValue().asText()
                                )
                );

        MetaModel newMetaModel = new MetaModel(type, action, method);

        newMetaModel.setData(data);
        newMetaModel.setHeader(header);
        newMetaModel.setPath(path);
        newMetaModel.setQuery(query);
        newMetaModel.setSelector(selector);
        return newMetaModel;
    }


    private String getStringNode(JsonNode node, String name) {
        return node.get(name) == null ? "" : node.get(name).asText();
    }

    private Iterator<Map.Entry<String, JsonNode>> getMapNode(JsonNode node, String name) {
        return node.get(name) == null ? Collections.emptyIterator() : node.get(name).fields();
    }


}
