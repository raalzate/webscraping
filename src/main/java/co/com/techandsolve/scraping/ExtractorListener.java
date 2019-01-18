package co.com.techandsolve.scraping;

import co.com.techandsolve.scraping.state.ModelState;
import org.jsoup.nodes.Document;

public interface ExtractorListener {
    enum Type {
        CONNECTING("CONNECTING"),
        ERROR_CONNECTING("ERROR_CONNECTING"),
        PARSING("PARSING"),
        ERROR_PARSING("ERROR_PARSING"),
        EXECUTE("EXECUTE"),
        ERROR_EXECUTE("ERROR_EXECUTE"),
        EXTRACTOR_DOCUMENT("EXTRACTOR_DOCUMENT"),
        EXTRACTOR_ERROR_DOCUMENT("EXTRACTOR_ERROR_DOCUMENT")
        ;
        private final String text;
        Type(final String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }

    void onEvent(Type name, ModelState modelState, Document document);
}
