package bg.sofia.uni.fmi.mjt.airbnb.filter;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class LocationCriterion implements Criterion {
    private Location currentLocation;
    private double maxDistance;
    public LocationCriterion(Location currentLocation, double maxDistance){
        this.currentLocation=currentLocation;
        this.maxDistance=maxDistance;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    private boolean checkIfDistanceCorrect(Bookable bookable){
        if(bookable==null)
            return false;
        double x=bookable.getLocation().getX();
        double y=bookable.getLocation().getY();
        double radios= this.getMaxDistance();
        double line=Math.sqrt(Math.pow(x,2)+Math.pow(y,2));

        return line<=radios;
    }

    @Override
    public boolean check(Bookable bookable) {
        return checkIfDistanceCorrect(bookable);
    }
}
