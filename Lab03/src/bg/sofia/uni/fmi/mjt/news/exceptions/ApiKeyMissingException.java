package bg.sofia.uni.fmi.mjt.news.exceptions;

public class ApiKeyMissingException extends Exception {
    public ApiKeyMissingException(String message) {
        super(message);
    }
}