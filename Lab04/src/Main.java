import bg.sofia.uni.fmi.mjt.flightscanner.FlightScanner;
import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.Flight;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.RegularFlight;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Main {
    static final int CAPACITY = 60;

    public static void main(String[] args) {
        FlightScanner sc = new FlightScanner();
        Airport miami = new Airport("1");
        Airport chicago = new Airport("2");
        Airport boston = new Airport("3");
        Airport plovdiv = new Airport("4");
       
        RegularFlight fl1 = RegularFlight.of("#1", miami, chicago, CAPACITY);
        RegularFlight fl2 = RegularFlight.of("#2", miami, boston, CAPACITY);
        RegularFlight fl3 = RegularFlight.of("#3", miami, plovdiv, CAPACITY);
        RegularFlight fl4 = RegularFlight.of("#4", chicago, boston, CAPACITY);

        HashSet<Flight> flights = new HashSet<>();

        flights.add(fl1);
        flights.add(fl2);
        flights.add(fl3);
        flights.add(fl4);
        for (Flight flight : flights) {
            System.out.println(flight);
        }



       // Map<Airport, HashSet<Flight>> flights;




    }

}