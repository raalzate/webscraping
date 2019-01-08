package co.com.techandsolve.scraping.infra;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MetalModel {
    private String type;
    private String action;
    private String method;
    private String path;
    private String query;
    private String selector;
    private Map<String, String> data;
    private Map<String, String> header;


    public MetalModel(String type, String action, String method) {
        this.type = type;
        this.action = action;
        this.method = method;
        this.data = new HashMap<>();
        this.header = new HashMap<>();
        this.path = "";
        this.query = "";
        this.selector = "";
    }

    public String getType() {
        return type;
    }

    public String getAction() {
        return action + path + query;
    }

    public Map<String, String> getData() {
        return data;
    }

    protected void setData(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    protected void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        Optional.ofNullable(query)
                .filter(q -> !"".equals(q))
                .map(q -> q.replace("?", ""))
                .ifPresent(q ->
                        this.query = "?" + query
                );

    }

    public String getSelector() {
        return Optional.of(selector)
                .filter(q -> !"".equals(q))
                .orElse("body");
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    @Override
    public String toString() {
        return String.format("%s[%s]:%s --> %s", getMethod(), getAction(), getType(), getSelector());
    }
}
