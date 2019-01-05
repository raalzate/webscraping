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

    protected void setData(Map<String, String> data) {
        this.data = data;
    }

    protected void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public String getType() {
        return type;
    }

    public String getAction() {
        return action+path+query;
    }

    public Map<String, String> getData() {
        return data;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getMethod() {
        return method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setQuery(String query) {
        Optional.ofNullable(query)
                .filter(q -> !"".equals(q))
                .map(q -> q.replace("?", ""))
                .ifPresent(q ->
                    this.query = "?"+query
                );

    }

    public void setSelector(String selector){
        this.selector = selector;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public String getSelector() {
        return Optional.of(selector)
                .filter(q -> !"".equals(q))
                .orElse("body");
    }

    @Override
    public String toString() {
        return String.format("%s[%s]:%s --> %s", getMethod(), getAction(), getType(), getSelector());
    }
}
