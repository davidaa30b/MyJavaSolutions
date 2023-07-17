package bg.sofia.uni.fmi.mjt.airbnb;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.filter.Criterion;



public class Airbnb implements AirbnbAPI {
    private  final Bookable[] accommodations;

    public Airbnb(Bookable[] accommodations){
        this.accommodations =accommodations;
    }

    @Override
    public Bookable findAccommodationById(String id) {
        if(id==null){
            return null;
        }

        for (Bookable accommodation : accommodations) {
            if (accommodation.getId().equalsIgnoreCase((id))) {

                return accommodation;
            }
        }
    return null;
    }

    @Override
    public double estimateTotalRevenue() {
        double sum=0;
        for (Bookable accommodation : accommodations) {
            if (accommodation.isBooked()) {
                sum += accommodation.getTotalPriceOfStay();
            }
        }
        return sum;
    }

    @Override
    public long countBookings() {
        long count=0;
        for (Bookable accommodation : accommodations) {
            if (accommodation.isBooked()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Bookable[] filterAccommodations(Criterion... criteria) {
        if(criteria.length==0 ){
            return accommodations;
        }
        Bookable[] result=new Bookable[accommodations.length];
        int passedCriteria;
        int index=0;
        for (Bookable accommodation : accommodations) {
            passedCriteria = 0;
            for (Criterion criterion : criteria) {
                if (criterion.check(accommodation) && criterion != null)
                    passedCriteria++;

                if (passedCriteria == criteria.length) {
                    result[index] = accommodation;
                    index++;
                }
            }
        }
        Bookable[] copy=new Bookable[index];
        for(int i=0;i<index;i++)
            copy[i]=result[i];

        return copy;
    }
}
