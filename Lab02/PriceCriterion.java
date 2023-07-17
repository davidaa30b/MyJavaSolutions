package bg.sofia.uni.fmi.mjt.airbnb.filter;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;

public class PriceCriterion implements Criterion{
    private final double minPrice;
    private final double maxPrice;

    public PriceCriterion(double minPrice, double maxPrice){
        this.minPrice=minPrice;
        this.maxPrice=maxPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public boolean checkIfPriceCorrect(Bookable bookable) {
    return this.getMinPrice()<=bookable.getPricePerNight() && this.getMaxPrice()>=bookable.getPricePerNight();
    }

    @Override
    public boolean check(Bookable bookable) {
        if(bookable==null)
            return false;
        return checkIfPriceCorrect(bookable);
    }
}
