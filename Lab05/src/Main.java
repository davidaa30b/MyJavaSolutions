import bg.sofia.uni.fmi.mjt.smartfridge.SmartFridge;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Stored;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType.FOOD;

public class Main {
    public static void main(String[] args) {
        SmartFridge fridge= new SmartFridge(12);

        Stored it1 = new Stored(FOOD,"banana",LocalDate.of(2022,12,1));
        Stored it2 = new Stored(FOOD,"apple",LocalDate.of(2023,1,19));
        Stored it3 = new Stored(FOOD,"cookies",LocalDate.of(2023,2,25));
        Stored it4 = new Stored(FOOD,"steak",LocalDate.of(2022,11,18));
        Stored it8 = new Stored(FOOD,"steak",LocalDate.of(2021,11,4));
        Stored it9 = new Stored(FOOD,"steak",LocalDate.of(2020,11,8));
        Stored it10 = new Stored(FOOD,"steak",LocalDate.of(2023,1,1));

        Stored it5= new Stored(FOOD,"fish",LocalDate.of(2022,12,10));
        Stored it6 = new Stored(FOOD,"banana",LocalDate.of(2021,4,8));
        Stored it7 = new Stored(FOOD,"banana",LocalDate.of(2024,7,5));

        try {
            fridge.store(it1,1);
            fridge.store(it2,1);
            fridge.store(it3,1);
            fridge.store(it4,1);
            fridge.store(it5,1);
            fridge.store(it6,1);
            fridge.store(it7,1);
            fridge.store(it8,1);
            fridge.store(it9,1);
            fridge.store(it10,2);

        }
        catch(Exception e){
            System.out.print(e.toString());
        }
        fridge.displayStoredItems();

        Set<Ingredient<Storable>> ingredientsWithinItems = new HashSet<>();
        Set<String> nameOfItems = new HashSet<>();
        for (Storable item : fridge.getItems()
        ) {
            if(!nameOfItems.contains(item.getName()) && !item.isExpired()) {
                nameOfItems.add(item.getName());
                ingredientsWithinItems.add(new DefaultIngredient<>(item, fridge.getQuantityOfItem(item.getName())));
            }
        }

        System.out.println("Show inside nameOfItems");

        for (String name : nameOfItems
             ) {
            System.out.println(name);
        }

        System.out.println("Show inside ingredientsWithinItems");

        for (Ingredient<Storable> ingredient : ingredientsWithinItems
        ) {
            System.out.println(ingredient.item() + " and its quantity is " + ingredient.quantity());
        }

    }
}