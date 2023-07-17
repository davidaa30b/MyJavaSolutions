package bg.sofia.uni.fmi.mjt.myfitnesspal;

import bg.sofia.uni.fmi.mjt.myfitnesspal.diary.FoodEntry;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FoodEntryTest {
    @Test
    void testFoodEntryNullFood() {
        NutritionInfo info = new NutritionInfo(20,40,40);
        assertThrows(IllegalArgumentException.class,()->new FoodEntry(null,1,info),
        "Food can not be null, exception expected to be thrown");
    }

    @Test
    void testFoodEntryEmptyFood() {
        NutritionInfo info = new NutritionInfo(20,40,40);
        assertThrows(IllegalArgumentException.class,()->new FoodEntry("",1,info),
                "Food can not be empty, exception expected to be thrown");
    }
    @Test
    void testFoodEntryBlankFood() {
        NutritionInfo info = new NutritionInfo(20,40,40);
        assertThrows(IllegalArgumentException.class,()->new FoodEntry(" ",1,info),
                "Food can not be blank, exception expected to be thrown");
    }
    @Test
    void testFoodEntryNegativeServingSize() {
        NutritionInfo info = new NutritionInfo(20,40,40);
        assertThrows(IllegalArgumentException.class,()->new FoodEntry("name",-2.2,info),
                "Serving size can not be negative, exception expected to be thrown");
    }

    @Test
    void testFoodEntryNullNutritionInfo() {
        assertThrows(IllegalArgumentException.class,()->new FoodEntry("name",2.2,null),
                "Serving size can not be negative, exception expected to be thrown");
    }

}
