package bg.sofia.uni.fmi.mjt.smartfridge.storable;

import java.util.Comparator;

public class StoredExpiredDateComparator implements Comparator<Storable> {
    @Override
    public int compare(Storable first, Storable second) {
        return first.getExpiration().compareTo(second.getExpiration());
    }
}
