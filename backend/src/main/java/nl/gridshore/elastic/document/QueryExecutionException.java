package nl.gridshore.elastic.document;

/**
 * Exception thrown when elastic throws an error.
 */
public class QueryExecutionException extends RuntimeException {
    public QueryExecutionException(String message) {
        super(message);
    }

    public QueryExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
