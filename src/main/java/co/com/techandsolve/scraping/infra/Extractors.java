package co.com.techandsolve.scraping.infra;

import co.com.techandsolve.scraping.DocumentPort;
import co.com.techandsolve.scraping.exception.ExtractorException;
import co.com.techandsolve.scraping.selector.HtmlSelector;
import co.com.techandsolve.scraping.Selector;
import co.com.techandsolve.scraping.state.ModelState;
import co.com.techandsolve.scraping.utils.MetalModelFileUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class Extractors {
    private Map<String, Consumer<MetaModel>> extractorsList;
    private ModelState modelState;


    Extractors(Map<String, Consumer<MetaModel>> extractorsList, ModelState modelState) {
        this.extractorsList = extractorsList;
        this.modelState = modelState;
        Optional.ofNullable(modelState)
                .orElseThrow(() -> new ExtractorException("Se debe definir primero el estado del modelo para almacenar los resultado, " +
                        "ver el mentodo setState(ModelState) de la clase Extractors."));
    }

    public static ExtractorsBuilder builder(DocumentPort port) {
        return new ExtractorsBuilder(port);
    }

    public void run(String file) {
        final JsonNode jsonNode = MetalModelFileUtils.getMetadata(file);

        Optional.ofNullable(jsonNode)
                .orElseThrow(() -> new ExtractorException("No se encontro ninguna información del archivo, verificar bien la ruta => "+file));

        final ParseModel parseModel = new ParseModel(jsonNode);

        extractorsList.keySet().forEach(name -> {
            try{
                MetaModel model = parseModel.toParse(name);
                MetaModel stateModel = modelState.getMetaModel();
                model.setQuery(stateModel.getQuery());
                model.setPath(stateModel.getPath());
                model.getData().putAll(stateModel.getData());
                model.getHeader().putAll(stateModel.getHeader());
                modelState.setStateModel(model);
                extractorsList.get(name).accept(model);
            } catch (Exception e){
                throw new ExtractorException("Se presenta una incisistencia con parse de la meta data, revisar que el key =>"+name);
            }

        });
    }

    public void run() {
        run(null);
    }

    public void runWithModel(MetaModel model) {
        MetaModel stateModel = modelState.getMetaModel();
        extractorsList.keySet().forEach(name -> {
            model.setQuery(stateModel.getQuery());
            model.setPath(stateModel.getPath());
            model.getData().putAll(stateModel.getData());
            model.getHeader().putAll(stateModel.getHeader());
            modelState.setStateModel(model);
            extractorsList.get(name).accept(model);
        });
    }

    public static class ExtractorsBuilder {

        private LinkedHashMap<String, Consumer<MetaModel>> extractorsList;
        private DocumentPort port;
        private ModelState modelState;

        ExtractorsBuilder(DocumentPort port) {
            this.port = port;
        }

        public ExtractorsBuilder setState(ModelState modelState) {
            this.modelState = modelState;
            return this;
        }


        public ExtractorsBuilder step(String name) {
            return step(name, new HtmlSelector());
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

        public Extractors buildExtractor(String name){
            return buildExtractor(name, new HtmlSelector());
        }

        public Extractors buildExtractor(String name, Selector<Element> func) {
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
            return build();
        }


        public Extractors build() {
            return new Extractors(extractorsList, modelState);
        }

    }
}
