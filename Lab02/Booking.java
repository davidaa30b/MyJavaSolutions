package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class Booking implements Bookable,CreateName{
    private LocalDateTime checkIn=null;
    private LocalDateTime checkOut=null;
    private final Location location;
    private final double pricePerNight;
    private  boolean booked;

    public Booking(Location location, double pricePerNight){
        this.location=location;
        this.pricePerNight=pricePerNight;
        booked=false;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public boolean isBooked() {
        return booked;
    }

    @Override
    public boolean book(LocalDateTime checkIn, LocalDateTime checkOut) {
        if(isBooked()==true)
            return false;
        if(checkIn==null || checkOut==null)
            return false;
        if(checkIn.isAfter(checkOut))
            return false;
        LocalDateTime today = LocalDateTime.now();
        if(checkIn.isBefore(today))
            return false;

        if(checkIn.isBefore(checkOut)) {
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            booked=true;
            return true;
        }
        return false;
    }

    @Override
    public double getTotalPriceOfStay() {
        if(isBooked()==false)
            return 0;

        double days= ChronoUnit.DAYS.between(checkIn,checkOut);
        return days*getPricePerNight();
    }

    @Override
    public double getPricePerNight() {
        return pricePerNight;
    }
}
