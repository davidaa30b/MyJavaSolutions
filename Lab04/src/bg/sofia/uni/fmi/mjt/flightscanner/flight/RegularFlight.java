package bg.sofia.uni.fmi.mjt.flightscanner.flight;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.FlightCapacityExceededException;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.InvalidFlightException;
import bg.sofia.uni.fmi.mjt.flightscanner.passenger.Passenger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.flightscanner.StringValidity.checkStringNotValid;

public class RegularFlight implements  Flight {
    private String flightId;
    private Airport from;
    private Airport to;
    private int totalCapacity;

    private List<Passenger> passengers;
    private int countOfPassengers;


    private RegularFlight(String flightId, Airport from, Airport to, int totalCapacity) {
        this.flightId = flightId;
        this.from = from;
        this.to = to;
        this.totalCapacity = totalCapacity;
        passengers = new ArrayList<>(totalCapacity);
        this.countOfPassengers = 0;
    }

    public static RegularFlight of(String flightId, Airport from, Airport to, int totalCapacity) {
        if (checkStringNotValid(flightId) || from == null || to == null || totalCapacity < 0) {
            throw new IllegalArgumentException();
        }

        if (from.id().equals(to.id())) {
            throw new InvalidFlightException();
        }

        return new RegularFlight(flightId, from, to, totalCapacity);

    }



    @Override
    public Airport getFrom() {
        return this.from;
    }

    @Override
    public Airport getTo() {
        return this.to;
    }

    @Override
    public void addPassenger(Passenger passenger) throws FlightCapacityExceededException {
        if (++countOfPassengers > totalCapacity) {
            throw new FlightCapacityExceededException();
        }
        passengers.add(passenger);
    }

    @Override
    public void addPassengers(Collection<Passenger> passengers) throws FlightCapacityExceededException {
        if (countOfPassengers + passengers.size() > totalCapacity) {
            throw new FlightCapacityExceededException();
        }
        this.passengers.addAll(passengers);
        countOfPassengers += passengers.size();
    }

    @Override
    public Collection<Passenger> getAllPassengers() {
        Collection<Passenger> passengersCopy = Collections.unmodifiableCollection(passengers);
        //final List<Passenger> passengersCopy = new ArrayList<>(passengers);

        return passengersCopy;
    }

    @Override
    public int getFreeSeatsCount() {
        return totalCapacity - countOfPassengers;
    }



}
