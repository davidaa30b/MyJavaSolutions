package bg.sofia.uni.fmi.mjt.flightscanner.passenger;


import static bg.sofia.uni.fmi.mjt.flightscanner.StringValidity.checkStringNotValid;

public record Passenger(String id, String name, Gender gender) {
    public Passenger {
        if (checkStringNotValid(id) || checkStringNotValid(name)) {
            throw new IllegalArgumentException();
        }
    }
}
