package bg.sofia.uni.fmi.mjt.news.exceptions;

public class NewsAPIWrapperException extends Exception {
    public NewsAPIWrapperException(String message) {
        super(message);
    }

    public NewsAPIWrapperException(String message, Exception e) {
        super(message, e);
    }
}
