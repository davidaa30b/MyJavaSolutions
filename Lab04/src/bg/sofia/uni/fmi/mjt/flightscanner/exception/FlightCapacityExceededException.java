package bg.sofia.uni.fmi.mjt.flightscanner.exception;

public class FlightCapacityExceededException extends Exception {
    public FlightCapacityExceededException() {
        super("There is no room for more passengers.");
    }
}
