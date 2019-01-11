package co.com.techandsolve.scraping.exception;

public class DocumentException extends RuntimeException {
    public DocumentException(final String message,  final Throwable cause) {
        super(message, cause);
    }
}
