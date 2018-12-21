package co.com.techandsolve.scraping;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class ModelState {
    private String model;
    private Object object;

    public void setState(String model, Object object) {
        this.object = object;
        this.model = model;
    }

    public ModelMemento save() {
        return new ModelMemento(model, object);
    }

    public void restoreToState(ModelMemento memento) {
        object = memento.getObject();
        model = memento.getModel();
    }

}
