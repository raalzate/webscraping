package co.com.techandsolve.scraping;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class ModelMemento {
    private String model;
    private Object object;

    public ModelMemento(String model, Object object) {
        this.model = model;
        this.object = object;
    }

    public String getModel() {
        return model;
    }

    public Object getObject() {
        return object;
    }
}
