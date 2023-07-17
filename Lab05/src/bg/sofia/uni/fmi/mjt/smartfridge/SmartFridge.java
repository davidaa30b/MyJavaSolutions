package bg.sofia.uni.fmi.mjt.smartfridge;

import bg.sofia.uni.fmi.mjt.smartfridge.exception.FridgeCapacityExceededException;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.InsufficientQuantityException;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.StoredExpiredDateComparator;

import java.util.*;

public class SmartFridge implements SmartFridgeAPI {

    private List<Storable> items;
    private final int totalCapacity;
    private int countOfItems;

    private List<Ingredient<Storable>> ingredientsFromCurFridge = new ArrayList<>();

    private  List<Storable> expired = new ArrayList<>();
    public SmartFridge(int totalCapacity) {
        System.out.println("New fridge is created with capacity of : " + totalCapacity);
        this.totalCapacity = totalCapacity;
        items = new ArrayList<>(totalCapacity);
        countOfItems = 0;
    }

    private boolean checkIfStringInvalid(String string) {
        return string == null || string.isEmpty() || string.isBlank();
    }

    private boolean checkIfQuantityInvalid(int quantity) {
        return quantity <= 0;
    }

    @Override
    public <E extends Storable> void store(E item, int quantity) throws FridgeCapacityExceededException {

       // System.out.println("Storing " + item.getName() + " with a quantity of : " + quantity);
        if (item == null) {
            System.out.println("Here");
            throw new IllegalArgumentException();
        }

        if ( checkIfQuantityInvalid(quantity)  ) {
            System.out.println("Error with addition");
            throw new IllegalArgumentException();
        }



        if (countOfItems + quantity > totalCapacity) {
            System.out.println("Over Capacity");
            throw new FridgeCapacityExceededException();
        }

        countOfItems += quantity;

        for (int i = 0; i < quantity; i++) {

            items.add(item);
        }
        ingredientsFromCurFridge.add(new DefaultIngredient<>(item, quantity));
       // System.out.println("Current count is : " + countOfItems);

    }

    @Override
    public List<? extends Storable> retrieve(String itemName) {

        if (checkIfStringInvalid(itemName)) {
            System.out.println("Error with retrieving");

            throw new IllegalArgumentException();
        }
        //System.out.println("Current state of items : ");
       // displayStoredItems();
      //  System.out.println("Retrieving " + itemName );
        List<Storable> retrieved = new ArrayList<>();


        for (Storable item : items) {
            if (item.getName().equals(itemName)) {
                retrieved.add(item);

            }
        }

        for (Storable item : expired) {
            if (item.getName().equals(itemName)) {
                retrieved.add(item);

            }
        }

        retrieved.sort(new StoredExpiredDateComparator());
        countOfItems -= retrieved.size();
        for (Storable retrievedItem : retrieved
        ) {
            items.remove(retrievedItem);
        }
        return retrieved;
    }

    //just for test

    public void displayStoredItems() {
        for (Storable item:items) {
            System.out.println("[Name : " + item.getName() + " ,Type : " + item.getType() +
                    " ,Expiration Date : " + item.getExpiration() + " ,Expired: " + item.isExpired());
        }
    }

    public List<Storable> getItems() {
        return items;
    }

    //just for test

    @Override
    public List<? extends Storable> retrieve(String itemName, int quantity) throws InsufficientQuantityException {

        if (checkIfStringInvalid(itemName) || checkIfQuantityInvalid(quantity)) {
        //    System.out.println("Error with retrieving");

            throw new IllegalArgumentException();
        }

      //  System.out.println("Current state of items : ");
       // displayStoredItems();


     //   System.out.println("Retrieving " + itemName + " with a quantity of : " + quantity );

        List<Storable> retrieved = new ArrayList<>();

        boolean hasItem = false;
        for (Storable item : items) {
            if (item.getName().equals(itemName)) {
                retrieved.add(item);
                hasItem = true;
            }

            if (quantity == retrieved.size()) {
                break;
            }
        }
        if (!hasItem || retrieved.size() != quantity) {
          //  System.out.println("Inside InsufficientQuantityException");

            throw new InsufficientQuantityException();
        }
        //System.out.println("Retrieved Items");
       // for (Storable item: retrieved
      //       ) {
      //      System.out.println(item);
      //  }
      //  System.out.println("--------------");
        retrieved.sort(new StoredExpiredDateComparator());
    //    System.out.println("the count of retrieved is : " + retrieved.size());

        countOfItems -= retrieved.size();
        for (Storable retrievedItem : retrieved
             ) {
            items.remove(retrievedItem);
        }

     //   System.out.println("State of items after retrieving: ");
     //   displayStoredItems();

        return retrieved;
    }

    @Override
    public int getQuantityOfItem(String itemName) {
        if (checkIfStringInvalid(itemName)) {
            throw new IllegalArgumentException();
        }

        int quantityOfItem = 0;
        for (Storable item : items) {
            if (item.getName().equals(itemName)) {
                quantityOfItem++;
            }
        }

        for (Storable item : expired) {
            if (item.getName().equals(itemName)) {
                quantityOfItem++;
            }
        }
      //  System.out.println("Here is the quantity of " + itemName + " : " + quantityOfItem);

        return quantityOfItem;
    }

    @Override
    public Iterator<Ingredient<? extends Storable>> getMissingIngredientsFromRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException();
        }


        Set<Ingredient<? extends Storable>> ingredientsForRecipe = recipe.getIngredients();
        System.out.println("What is in the fridge with the strange list");
        for (var ingredient : ingredientsFromCurFridge
        ) {

            System.out.println("Name : " + ingredient.item().getName() + " ,Date : "
                    + ingredient.item().getExpiration() + " ,Expired : "
                    + ingredient.item().isExpired() + " ,Quantity : " + ingredient.quantity());
        }
        System.out.println("What is in the recipe");
        for (var ingredient : ingredientsForRecipe
             ) {

            System.out.println("Name : " + ingredient.item().getName() + " ,Date : "
                    + ingredient.item().getExpiration() + " ,Expired : "
                    + ingredient.item().isExpired() + " ,Quantity : " + ingredient.quantity());
        }
        //System.out.println("What is in the fridge");
        //displayStoredItems();

        Set<Ingredient<? extends Storable>> needToAdd = new HashSet<>();
        boolean ingredientFound = false;
        for (var ingredientRecipe : ingredientsForRecipe) {

            for (var ingredientFridge : ingredientsFromCurFridge) {
                if (ingredientRecipe.item().getName().equals(ingredientFridge.item().getName())
                        && !ingredientFridge.item().isExpired()) {
                    ingredientFound = true;
                    if (ingredientRecipe.quantity() > ingredientFridge.quantity()) {
                        needToAdd.add(new DefaultIngredient<>(ingredientRecipe.item(),
                                ingredientRecipe.quantity() - ingredientFridge.quantity()));
                    }
                }

            }
            if (!ingredientFound) {
                needToAdd.add(new DefaultIngredient<>(ingredientRecipe.item(), ingredientRecipe.quantity()));
            }
        }

        Iterator iterator = needToAdd.iterator();

        if (needToAdd.isEmpty()) {
            return null;
        }
        else {
            return iterator;
        }
    }

    @Override
    public List<? extends Storable> removeExpired() {
        System.out.print("Calling to remove expired");
        for (Storable item : items) {
            if (item.isExpired()) {
                expired.add(item);
            }
        }
        countOfItems -= expired.size();
        items.removeAll(expired);
        return expired;
    }
}
