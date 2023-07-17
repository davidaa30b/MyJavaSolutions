package bg.sofia.uni.fmi.mjt.flightscanner.airport;

import static bg.sofia.uni.fmi.mjt.flightscanner.StringValidity.checkStringNotValid;

public record Airport(String id) {
    public Airport {
        if (checkStringNotValid(id)) {
            throw new IllegalArgumentException();
        }
    }

    //public int compareTo(Airport id) {
   //     return this.id().compareTo(id());
   // }
}
