package co.com.techandsolve.scraping.scraper;

import co.com.techandsolve.scraping.DocumentPort;
import co.com.techandsolve.scraping.ExtractorListener;
import co.com.techandsolve.scraping.Selector;
import co.com.techandsolve.scraping.exception.DocumentException;
import co.com.techandsolve.scraping.exception.ExtractorException;
import co.com.techandsolve.scraping.helper.MetalModelFileHelper;
import co.com.techandsolve.scraping.selector.HtmlSelector;
import co.com.techandsolve.scraping.state.ModelState;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class Extractors {
    private final Map<String, Consumer<MetaModel>> extractorsList;
    private final ModelState modelState;


    private Extractors(Map<String, Consumer<MetaModel>> extractorsList, ModelState modelState) {
        this.extractorsList = extractorsList;
        this.modelState = modelState;
        Objects.requireNonNull(modelState, "Se debe definir primero el estado del modelo para almacenar los resultado, " +
                "ver el metodo setState(ModelState) de la clase Extractors.");

    }

    public static ExtractorsBuilder builder(DocumentPort port) {
        return new ExtractorsBuilder(port);
    }

    public void run(String file) {
        final JsonNode jsonNode = MetalModelFileHelper.getMetadata(file);

        Objects.requireNonNull(jsonNode, "No se encontro ninguna información del archivo, verificar bien la ruta => " + file);

        final MetaModelUtils metaModelUtils = new MetaModelUtils(jsonNode);

        extractorsList.keySet().forEach(name -> {
            MetaModel model = null;
            try {
                model = metaModelUtils.toParse(name);
            } catch (Exception e) {
                throw new ExtractorException("Se presenta un problema con el parse de la meta data, revisar que el key =>" + name, e);
            }

            MetaModel stateModel = modelState.getMetaModel();
            model.setQuery(stateModel.getQuery());
            model.setPath(stateModel.getPath());
            model.getData().putAll(stateModel.getData());
            model.getHeader().putAll(stateModel.getHeader());
            modelState.setMetaModel(model);
            extractorsList.get(name).accept(model);

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
            modelState.setMetaModel(model);
            extractorsList.get(name).accept(model);
        });
    }

    public static class ExtractorsBuilder {

        private final DocumentPort port;

        private ExtractorListener extractorListener;
        private LinkedHashMap<String, Consumer<MetaModel>> extractorsList;
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
                Document document = protocol(metalModel);
                Element element = document.selectFirst(selector);
                publishEvent(ExtractorListener.Type.EXTRACTOR_DOCUMENT, document);
                try{
                    func.accept(name, modelState, element);
                } catch (RuntimeException e){
                    publishEvent(ExtractorListener.Type.EXTRACTOR_ERROR_DOCUMENT, document);
                    throw  new ExtractorException("Existe un problema en la ejecución del selector" , metalModel, document, e);
                }
            });
            return this;
        }

        public Extractors buildExtractor(String name) {
            return buildExtractor(name, new HtmlSelector());
        }

        private Document protocol(MetaModel metalModel){
            publishEvent(ExtractorListener.Type.CONNECTING, null);
            try {
                port.connect(metalModel);
            } catch (DocumentException e){
                publishEvent(ExtractorListener.Type.ERROR_CONNECTING, null);
                throw e;
            }
            publishEvent(ExtractorListener.Type.EXECUTE, null);
            try {
                port.execute();
            } catch (DocumentException e){
                publishEvent(ExtractorListener.Type.ERROR_EXECUTE, null);
                throw e;
            }
            publishEvent(ExtractorListener.Type.PARSING, null);
            try {
                return port.parse();
            } catch (DocumentException e){
                publishEvent(ExtractorListener.Type.ERROR_PARSING, null);
                throw e;
            }
        }

        public Extractors buildExtractor(String name, Selector<Element> func) {
            if (this.extractorsList == null) {
                this.extractorsList = new LinkedHashMap<>();
            }

            this.extractorsList.put(name, metalModel -> {
                String selector = metalModel.getSelector();
                Document document = protocol(metalModel);
                Element element = document.selectFirst(selector);
                publishEvent(ExtractorListener.Type.EXTRACTOR_DOCUMENT, document);
                try {
                    func.accept(name, modelState, element);
                } catch (RuntimeException e){
                    publishEvent(ExtractorListener.Type.EXTRACTOR_ERROR_DOCUMENT, document);
                    throw  new ExtractorException("Existe un problema en la ejecución del selector" , metalModel, document, e);
                }
            });
            return build();
        }

        public ExtractorsBuilder setExtractorListener(ExtractorListener extractorListener){
            this.extractorListener = extractorListener;
            return this;
        }

        private void publishEvent(ExtractorListener.Type type, Document document){
            Optional.ofNullable(extractorListener).ifPresent(e ->
                e.onEvent(type, modelState, document)
            );
        }

        public Extractors build() {
            return new Extractors(extractorsList, modelState);
        }

    }
}
