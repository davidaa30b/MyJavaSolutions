package bg.sofia.uni.fmi.mjt.smartfridge.storable;

import bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType;

import java.time.LocalDate;

public class Stored implements Storable {

    private final StorableType type;
    private final String name;
    private final LocalDate expirationDate;
   // private final boolean isExpired;

    public Stored(StorableType type, String name, LocalDate expirationDate/*,boolean isExpired*/) {
        this.type = type;
        this.name = name;
        this.expirationDate = expirationDate;
   // this.isExpired=isExpired;
    }

    @Override
    public StorableType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public LocalDate getExpiration() {
        return expirationDate;
    }

    @Override
    public boolean isExpired() {
        return this.getExpiration().compareTo(LocalDate.now()) <= 0;
        //return isExpired;
    }

    @Override
    public String toString() {
        return "[Name : " + name + " ,Type : " + type +
                " ,Expiration Date : " + expirationDate + " ,Expired: " + isExpired();
    }
}
