package co.com.techandsolve.scraping.infra;

import co.com.techandsolve.scraping.DocumentPort;
import co.com.techandsolve.scraping.selector.Selector;
import co.com.techandsolve.scraping.state.ModelState;
import co.com.techandsolve.scraping.utils.MetalModelFileUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class Extractors {
    private Map<String, Consumer<MetalModel>> extractorsList;
    private ModelState modelState;
    private Consumer<MetalModel> extractor;


    Extractors(Map<String, Consumer<MetalModel>> extractorsList, ModelState modelState, Consumer<MetalModel> extractor) {
        this.extractorsList = extractorsList;
        this.modelState = modelState;
        this.extractor = extractor;
    }

    public static ExtractorsBuilder builder(DocumentPort port) {
        return new ExtractorsBuilder(port);
    }

    public void run(String file) {
        final JsonNode jsonNode = MetalModelFileUtils.getMetadata(file);
        final ParseModel parseModel = new ParseModel(jsonNode);

        extractorsList.keySet().forEach(name -> {
            MetalModel model = parseModel.toParse(name);
            MetalModel stateModel = modelState.getMetaModel();
            model.setQuery(stateModel.getQuery());
            model.setPath(stateModel.getPath());
            model.getData().putAll(stateModel.getData());
            model.getHeader().putAll(stateModel.getHeader());
            modelState.setStateModel(model);
            extractorsList.get(name).accept(model);
        });
    }

    public void run() {
        run(null);
    }

    public void runWithModel(MetalModel model) {
        MetalModel stateModel = modelState.getMetaModel();
        model.setQuery(stateModel.getQuery());
        model.setPath(stateModel.getPath());
        model.getData().putAll(stateModel.getData());
        model.getHeader().putAll(stateModel.getHeader());
        modelState.setStateModel(model);
        extractor.accept(model);
    }

    public static class ExtractorsBuilder {

        private LinkedHashMap<String, Consumer<MetalModel>> extractorsList;
        private Consumer<MetalModel> extractor;
        private DocumentPort port;
        private ModelState modelState;

        ExtractorsBuilder(DocumentPort port) {
            this.port = port;
        }

        public ExtractorsBuilder setState(ModelState modelState) {
            this.modelState = modelState;
            return this;
        }

        public ExtractorsBuilder step(String name, Selector<Element> func) {
            if (this.extractorsList == null) {
                this.extractorsList = new LinkedHashMap<>();
            }

            this.extractorsList.put(name, metalModel -> {
                String selector = metalModel.getSelector();
                port.connect(metalModel);
                port.execute();
                Document document = port.parse();
                Element element = document.selectFirst(selector);
                func.accept(name, modelState, element);
            });
            return this;
        }

        public ExtractorsBuilder setSelector(String label, Selector<Element> func) {
            this.extractor = metalModel -> {
                String selector = metalModel.getSelector();
                port.connect(metalModel);
                port.execute();
                Document document = port.parse();
                Element element = document.selectFirst(selector);
                func.accept(label, modelState, element);
            };
            return this;
        }


        public Extractors build() {
            return new Extractors(extractorsList, modelState, extractor);
        }

    }
}
