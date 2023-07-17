package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

import java.time.LocalDateTime;


public class Apartment extends Booking{

    private  String id;
    private static int number=0;
    private int index=0;


    public Apartment(Location location,double pricePerNight){

        super(location,pricePerNight);

        this.index=number;
        this.id=getNameForId()+this.index;
        incrementNumber();
    }
    public static void incrementNumber(){
        number++;
    }

    @Override
    public String getNameForId() {

        return "APA-";
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public Location getLocation() {

        return super.getLocation();
    }

    @Override
    public boolean isBooked() {
        return super.isBooked();
    }

    @Override
    public boolean book(LocalDateTime checkIn, LocalDateTime checkOut) {
        return super.book(checkIn,checkOut);
    }

    @Override
    public double getTotalPriceOfStay() {
        return super.getTotalPriceOfStay();
    }

    @Override
    public double getPricePerNight() {

        return super.getPricePerNight();
    }


}
