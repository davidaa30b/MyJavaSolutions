package bg.sofia.uni.fmi.mjt.smartfridge.exception;

public class FridgeCapacityExceededException extends Exception {

    public FridgeCapacityExceededException() {
        super("There is no room for more items.");
    }
}
