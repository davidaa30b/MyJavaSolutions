package bg.sofia.uni.fmi.mjt.flightscanner;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.Flight;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.FlightsByDestinationComparator;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.FlightsByFreeSeatsComparator;

import java.util.*;

public class FlightScanner implements FlightScannerAPI {

    private final Map<Airport, HashSet<Flight>> flights;

    public FlightScanner() {
        flights = new HashMap<>();
    }

    @Override
    public void add(Flight flight) {

        if (flight == null) {
            throw new IllegalArgumentException();
        }
        if (!flights.containsKey(flight.getFrom())) {
            flights.put(flight.getFrom(), new HashSet<Flight>());
        }

        if (!flights.containsKey(flight.getTo())) {
            flights.put(flight.getTo(), new HashSet<Flight>());
        }

        flights.get(flight.getFrom()).add(flight);
        flights.get(flight.getTo()).add(flight);
    }


    @Override
    public void addAll(Collection<Flight> flights) {

        if (flights == null) {
            throw new IllegalArgumentException();
        }

        for (Flight flight : flights) {
            Airport start = flight.getFrom();
            Airport destination = flight.getTo();

            this.flights.putIfAbsent(start, new HashSet<Flight>());
            this.flights.putIfAbsent(destination, new HashSet<Flight>());

            this.flights.get(start).add(flight);
            this.flights.get(destination).add(flight);
        }
    }

    @Override
    public List<Flight> searchFlights(Airport from, Airport to) {
        if (from == null || to == null || from.id().equals(to.id())) {
            throw new IllegalArgumentException();
        }

        Queue<Flight> flightsQueue = new LinkedList<>();
        Set<Airport> visited = new HashSet<>();
        Map<Airport, Flight> cameFrom = new HashMap<>();

        Airport current = from;
        flightsQueue.add(null); // doesn't work with ArrayDeque
        cameFrom.put(from, null);
        visited.add(from);

        while (!current.equals(to)) {
            for (Flight flight : flights.get(current)) {
                if (!visited.contains(flight.getTo())) {
                    flightsQueue.add(flight);
                    visited.add(flight.getTo());
                }
            }

            flightsQueue.poll();

            // The destination is not reachable
            if (flightsQueue.isEmpty()) {
                return new ArrayList<>();
            }

            current = flightsQueue.peek().getTo();
            cameFrom.put(current, flightsQueue.peek());
        }

        if (cameFrom.isEmpty()) {
            return new ArrayList<>();
        }

        LinkedList<Flight> result = new LinkedList<>();
        Flight cameWith = cameFrom.get(to);

        while (cameWith != null) {
            result.addFirst(cameWith);
            cameWith = cameFrom.get(cameWith.getFrom());
        }

        return result;
    }


    @Override
    public List<Flight> getFlightsSortedByFreeSeats(Airport from) {
        if (null == from) {
            throw new IllegalArgumentException();
        }
        if (!flights.containsKey(from) || flights.get(from).isEmpty()) {
            return new ArrayList<>();
        }

        List<Flight> result = new ArrayList<>(flights.get(from));
        result.sort(new FlightsByFreeSeatsComparator());

        return List.copyOf(result);
    }


    @Override
    public List<Flight> getFlightsSortedByDestination(Airport from) {
        if (null == from) {
            throw new IllegalArgumentException();
        }

        if (!flights.containsKey(from) || flights.get(from).isEmpty()) {
            return new ArrayList<>();
        }

        List<Flight> result = new ArrayList<>(flights.get(from));
        result.sort(new FlightsByDestinationComparator());

        return List.copyOf(result);
    }
}
