package co.com.techandsolve.scraping.exception;

import co.com.techandsolve.scraping.scraper.MetaModel;
import org.jsoup.nodes.Document;

public class ExtractorException extends RuntimeException {

    private final transient  MetaModel metaModel;
    private final transient  Document document;


    public ExtractorException(final String message, final MetaModel metaModel, final Document document, final Throwable cause) {
        super(message, cause);
        this.metaModel = metaModel;
        this.document = document;
    }

    public ExtractorException(final String message, final MetaModel metaModel,  final Throwable cause) {
        this(message, metaModel, null, cause);
    }

    public ExtractorException(final String message, final Throwable cause) {
        this(message, null, null, cause);
    }

    public ExtractorException(final String message, final MetaModel metaModel) {
        this(message, metaModel, null, null);
    }

    public ExtractorException(final String message) {
        this(message, null, null, null);
    }

    public MetaModel getMetaModel() {
        return metaModel;
    }

    public Document getDocument() {
        return document;
    }

}
