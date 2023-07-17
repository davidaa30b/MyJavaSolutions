package bg.sofia.uni.fmi.mjt.cocktail.server;

import bg.sofia.uni.fmi.mjt.cocktail.server.Command;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.CocktailStorage;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.HashSet;
import java.util.Set;

public class CommandExecutor {
    private static final String INVALID_ARGS_COUNT_MESSAGE_FORMAT =
            "Invalid count of arguments: \"%s\" expects %d arguments. Example: \"%s\"";
    private static final String CREATE = "create";
    private static final String GET_ALL = "get all";
    private static final String GET_BY_NAME = "get by-name";
    private static final String GET_BY_INGREDIENT = "get by-ingredient";

    private CocktailStorage storage;



    public CommandExecutor(CocktailStorage storage) {
        this.storage = storage;
    }

    public String execute(Command cmd) {
        return switch (cmd.command()) {
            case CREATE -> createCocktailCommand(cmd.arguments());
            case GET_BY_INGREDIENT -> getCocktailsWithIngredientCommand(cmd.arguments());
            case GET_ALL-> getAllCocktailsCommand();
            case GET_BY_NAME -> getRecipeForCocktailByNameCommand(cmd.arguments());
            default -> "Unknown command";
        };
    }



    private String createCocktailCommand(String[] args) {

        String cocktailName = args[0];
        Set<Ingredient> ingredientsForCocktail = new HashSet<>();
        for (int i = 1; i < args.length; i++) {
            if ( i % 2 == 0 ) {
                ingredientsForCocktail.add(new Ingredient(args[i - 1], args[i]));
            }
        }
        try {
            storage.createCocktail(new Cocktail(cocktailName, ingredientsForCocktail) );
        } catch (CocktailAlreadyExistsException e) {
            return String.format("{\"status\":\"ERROR\",\"errorMessage\":\"cocktail %s already exists\"}",
                    cocktailName);
        }

        return String.format("{\"status\":\"CREATED\"}");
    }
    
    private String getRecipeForCocktailByNameCommand(String[] args) {
        String cocktailName = args[0];
        Cocktail theCocktail;

        try {
            theCocktail = storage.getCocktail(cocktailName);
        } catch (CocktailNotFoundException e) {
            return String.format("{\"status\":\"ERROR\",\"errorMessage\":\"cocktail %s does not exist\"}",
                    cocktailName);
        }

        String totalInfo = "[";

        String subInfo = "[";
        for (var ingredient : theCocktail.ingredients()) {
            subInfo += String.format("{\"name\":\"%s\",\"amount\":\"%s\"},", ingredient.name(),
                    ingredient.amount());
        }
        subInfo = subInfo.substring(0, subInfo.length() - 1);
        subInfo += "]";
        totalInfo += String.format("{\"name\":\"%s\",\"ingredients\":%s},", theCocktail.name(), subInfo);

        totalInfo = totalInfo.substring(0, totalInfo.length() - 1);
        totalInfo += "]";
        String response = String.format("{\"status\":\"OK\",\"cocktails\":%s}", totalInfo);

        return response;
    }

    private String getCocktailsWithIngredientCommand(String[] args) {

        String ingredientName = args[0];
        boolean flag = false;
        String totalInfo = "[";
        for (var cocktail : storage.getCocktailsWithIngredient(ingredientName)) {
            String subInfo = "[";
            for (var ingredient : cocktail.ingredients()) {
                subInfo += String.format("{\"name\":\"%s\",\"amount\":\"%s\"},",
                        ingredient.name(), ingredient.amount());
            }
            subInfo = subInfo.substring(0, subInfo.length() - 1);
            subInfo += "]";
            totalInfo += String.format("{\"name\":\"%s\",\"ingredients\":%s},",
                    cocktail.name(), subInfo);
            flag = true;
        }
        if (flag) {
            totalInfo = totalInfo.substring(0, totalInfo.length() - 1);
        }
        totalInfo += "]";
        String response = String.format("{\"status\":\"OK\",\"cocktails\":%s}",
                totalInfo);

        return response;
    }

    private String getAllCocktailsCommand() {

        String totalInfo = "[";
        boolean flag = false;

        for (var cocktail : storage.getCocktails() ) {
            String subInfo = "[";
            for ( var ingredient : cocktail.ingredients()) {
                subInfo += String.format("{\"name\":\"%s\",\"amount\":\"%s\"},",
                        ingredient.name(),
                        ingredient.amount());
            }
            subInfo = subInfo.substring(0, subInfo.length() - 1);
            subInfo += "]";
            totalInfo += String.format("{\"name\":\"%s\",\"ingredients\":%s},",
                    cocktail.name(),
                    subInfo);
            flag = true;

        }
        if (flag) {
            totalInfo = totalInfo.substring(0, totalInfo.length() - 1);
        }
        totalInfo += "]";
        String response = String.format("{\"status\":\"OK\",\"cocktails\":%s}", totalInfo);


        return response;
    }
}