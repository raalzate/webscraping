package co.com.techandsolve.scraping;

import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class Extractors {
    private Map<String, Consumer<ParseModel>> extractorsList;

    Extractors(Map<String, Consumer<ParseModel>> extractorsList) {
        this.extractorsList = extractorsList;
    }

    public static ExtractorsBuilder builder() {
        return new ExtractorsBuilder();
    }

    public void run() {
        final JsonNode jsonNode = ReadMetalModelFile.getMetadata();
        final ParseModel parseModel = new ParseModel(jsonNode);
        extractorsList.values()
                .forEach(parseModelConsumer ->
                        parseModelConsumer.accept(parseModel));
    }

    public static class ExtractorsBuilder {

        private LinkedHashMap<String, Consumer<ParseModel>> extractorsList;

        private String docType;
        private String docId;

        ExtractorsBuilder() {
        }

        public ExtractorsBuilder setDNI(String type, String id) {
            this.docType = type;
            this.docId = id;
            return this;
        }

        public ExtractorsBuilder step(String name, Consumer<Document> func) {
            if (this.extractorsList == null) {
                this.extractorsList = new LinkedHashMap<>();
            }

            this.extractorsList.put(name, parseModel -> {
                parseModel.setDNI(docType, docId);
                ParseModel.Model model = parseModel.toParse(name);
                try {
                    Connection.Response response = Jsoup.connect(model.getAction())
                            .proxy(SessionUtils.getProxy())
                            .cookies(SessionUtils.getCookies())
                            .method(model.getMethod())
                            .data(model.getData())
                            .followRedirects(true)
                            .execute();
                    func.accept(response.parse());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return this;
        }


        public Extractors build() {
            return new Extractors(extractorsList);
        }

    }
}
