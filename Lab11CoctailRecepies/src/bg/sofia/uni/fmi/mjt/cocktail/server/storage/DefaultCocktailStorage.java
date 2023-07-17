package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.*;

public class DefaultCocktailStorage implements CocktailStorage  {

    private Set<Cocktail> cocktailRecipes;

    public DefaultCocktailStorage() {
        cocktailRecipes = new HashSet<>();
    }

    @Override
    public void createCocktail(Cocktail cocktail) throws CocktailAlreadyExistsException {
        Cocktail toFind = cocktailRecipes.stream()
                .filter(other->other.name()
                        .equals(cocktail.name()))
                .findAny()
                .orElse(null);

        if (toFind != null) {
            throw new CocktailAlreadyExistsException();
        }

        cocktailRecipes.add(cocktail);

    }

    @Override
    public Collection<Cocktail> getCocktails() {
        return cocktailRecipes;
    }

    @Override
    public Collection<Cocktail> getCocktailsWithIngredient(String ingredientName) {
        Set<Cocktail> toReturn = new HashSet<>();

        for (var cocktail : cocktailRecipes) {
            for (var ingredient : cocktail.ingredients()) {
                if (ingredient.name().equalsIgnoreCase(ingredientName)) {
                    toReturn.add(cocktail);
                }
            }
        }

        return toReturn;
    }

    @Override
    public Cocktail getCocktail(String name) throws CocktailNotFoundException {

        for (var cocktail : cocktailRecipes) {
            if (cocktail.name().equalsIgnoreCase(name)) {
                return cocktail;
            }
        }

        throw new CocktailNotFoundException();
    }
}
