package bg.sofia.uni.fmi.mjt.flightscanner;

public class StringValidity {
    public static boolean checkStringNotValid(String flightId) {
        return flightId == null || flightId.isEmpty() || flightId.isBlank();
    }
}
