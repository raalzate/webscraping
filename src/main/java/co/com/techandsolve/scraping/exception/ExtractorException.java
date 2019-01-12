package co.com.techandsolve.scraping.exception;

public class ExtractorException extends RuntimeException {
    public ExtractorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExtractorException(final String message) {
        super(message);
    }
}
