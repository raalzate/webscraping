package co.com.techandsolve.scraping.state;

import co.com.techandsolve.scraping.infra.MetalModel;
import co.com.techandsolve.scraping.infra.ParseModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class ModelState {
    private MetalModel model;
    private ExtraData extraData;

    public ModelState() {
        this.model = ParseModel.getModel();
        this.extraData = new ExtraData();
    }

    public MetalModel getMetaModel() {
        return model;
    }


    public ExtraData setStateModel(MetalModel model) {
        Optional.ofNullable(model).ifPresent(m -> this.model = m);
        return extraData;
    }

    public Map<String, Object> getExtra() {
        return extraData.extra;
    }

    public static class ExtraData {
        private Map<String, Object> extra;

        ExtraData() {
            this.extra = new HashMap<>();
        }

        public ExtraData putExtra(String key, Object value) {
            extra.put(key, value);
            return this;
        }
    }

}
