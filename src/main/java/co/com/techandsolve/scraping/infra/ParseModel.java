package co.com.techandsolve.scraping.infra;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class ParseModel {

    private JsonNode jsonNode;

    public ParseModel(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public static MetalModel getModel(){
     return new MetalModel(null, null, null);
    }


    public MetalModel toParse(String nodeKey) {
        JsonNode node = jsonNode.get(nodeKey);

        String method = getStringNode(node, "method");
        String path = getStringNode(node, "path");
        String query = getStringNode(node, "query");
        String action = getStringNode(node, "action");
        String type = getStringNode(node, "type");
        String selector = getStringNode(node, "selector");
        Map<String, String> data = new HashMap<>();
        Map<String, String> header = new HashMap<>();

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

        MetalModel newMetalModel = new MetalModel(type, action, method);

        newMetalModel.setData(data);
        newMetalModel.setHeader(header);
        newMetalModel.setPath(path);
        newMetalModel.setQuery(query);
        newMetalModel.setSelector(selector);
        return newMetalModel;
    }


    private String getStringNode(JsonNode node, String name){
        return node.get(name) == null ? "": node.get(name).asText();
    }

    private Iterator<Map.Entry<String, JsonNode>> getMapNode(JsonNode node, String name){
        return node.get(name) == null ? Collections.emptyIterator(): node.get(name).fields();
    }


}
