package co.com.techandsolve.scraping.state;

import co.com.techandsolve.scraping.scraper.MetaModel;
import co.com.techandsolve.scraping.scraper.MetaModelUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class ModelState {

    private final ExtraData extraData;
    private MetaModel model;

    public ModelState() {
        this.model = MetaModelUtils.getModel();
        this.extraData = new ExtraData();
    }

    public MetaModel getMetaModel() {
        return model;
    }

    public ExtraData setMetaModel(MetaModel model) {
        Optional.ofNullable(model).ifPresent(m -> this.model = m);
        return extraData;
    }

    public Map<String, Object> getExtra() {
        return extraData.extra;
    }

    public static final class ExtraData {
        private Map<String, Object> extra;

        private ExtraData() {
            this.extra = new HashMap<>();
        }

        public ExtraData putExtra(String key, Object value) {
            extra.put(key, value);
            return this;
        }
    }


}
