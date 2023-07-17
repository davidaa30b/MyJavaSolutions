package bg.sofia.uni.fmi.mjt.smartfridge.exception;

public class InsufficientQuantityException extends Exception {

    public InsufficientQuantityException() {

        super("Insufficiently inserted data.");
    }
}
