package bg.sofia.uni.fmi.mjt.news.exceptions;

public class SourceDoesNotExistException extends Exception {
    public SourceDoesNotExistException(String message) {
        super(message);
    }
}