package bg.sofia.uni.fmi.mjt.myfitnesspal;

import bg.sofia.uni.fmi.mjt.myfitnesspal.diary.DailyFoodDiary;
import bg.sofia.uni.fmi.mjt.myfitnesspal.diary.FoodEntry;
import bg.sofia.uni.fmi.mjt.myfitnesspal.diary.Meal;
import bg.sofia.uni.fmi.mjt.myfitnesspal.exception.UnknownFoodException;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfoAPI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DailyFoodDiaryTest {

    @Mock
    private NutritionInfoAPI nutritionInfoApiMock;

    @InjectMocks
    private DailyFoodDiary dailyFoodDiary;// = new DailyFoodDiary(nutritionInfoApiMock);


    @Test
    void testAddFoodNegativeServingSize()throws UnknownFoodException {
        assertThrows(IllegalArgumentException.class, ()-> dailyFoodDiary.addFood(Meal.BREAKFAST,
                        "something",-10),
                "Serving size is not defined for negative numbers and should throw an exception");


    }

    @Test
    void testAddFoodNullMeal()throws UnknownFoodException {
        assertThrows(IllegalArgumentException.class, ()-> dailyFoodDiary.addFood
                        (null,"something",10),
                "Meal cannot be null and should throw an exception");
    }

    @Test
    void testAddFoodFoodNameNull()throws UnknownFoodException {
        assertThrows(IllegalArgumentException.class, ()-> dailyFoodDiary.addFood
                        (Meal.DINNER,null,10),
                "Food name cannot be null and should throw an exception");
    }

    @Test
    void testAddFoodFoodNameEmpty()throws UnknownFoodException {
        assertThrows(IllegalArgumentException.class, ()-> dailyFoodDiary.addFood(Meal.DINNER,
                        "",10),
                "Food name cannot be empty and should throw an exception");
    }

    @Test
    void testAddFoodFoodNameBlank()throws UnknownFoodException {
        assertThrows(IllegalArgumentException.class, ()-> dailyFoodDiary.addFood(Meal.DINNER,
                        " ",10),
                "Food name cannot be blank and should throw an exception");
    }



    @Test
    void testAddFoodWithCorrectReturn() throws UnknownFoodException {
        when(nutritionInfoApiMock.getNutritionInfo("steak")).
                thenReturn(new NutritionInfo(50,30,20));

        NutritionInfo nutritionInfo = nutritionInfoApiMock.getNutritionInfo("steak");

        assertEquals(new FoodEntry("steak", 1, nutritionInfo),
                dailyFoodDiary.addFood(Meal.DINNER,"steak",1));
    }

   @Test
    void testGetALLFoodEntriesEmptyCollection() {
       Collection<FoodEntry> expected= new ArrayList<>();
       assertIterableEquals(expected,dailyFoodDiary.getAllFoodEntries(),
               "Expected empty collection");
   }

   @Test
    void testGetAllFoodEntriesExistingFoodEntries()throws UnknownFoodException {
        Set<FoodEntry> expected = new HashSet<>();
        FoodEntry food1= new FoodEntry("Food1",1.2,
                new NutritionInfo(20,40,40));
        FoodEntry food2= new FoodEntry("Food2",2.2,
                new NutritionInfo(20,50,30));
        FoodEntry food3= new FoodEntry("Food3",3.2,
                new NutritionInfo(10,40,50));
        expected.add(food1);
        expected.add(food2);
        expected.add(food3);

        when(nutritionInfoApiMock.getNutritionInfo("Food1")).
                thenReturn(new NutritionInfo(20,40,40));
       when(nutritionInfoApiMock.getNutritionInfo("Food2")).
               thenReturn(new NutritionInfo(20,50,30));
       when(nutritionInfoApiMock.getNutritionInfo("Food3")).
               thenReturn(new NutritionInfo(10,40,50));

       dailyFoodDiary.addFood(Meal.DINNER,"Food1",1.2);
       dailyFoodDiary.addFood(Meal.DINNER,"Food2",2.2);
       dailyFoodDiary.addFood(Meal.DINNER,"Food3",3.2);

       Collection<FoodEntry> actual = dailyFoodDiary.getAllFoodEntries();
       assertTrue(expected.containsAll(actual) && actual.containsAll(expected),
               "Expected collection with 3 elements - Food1,Food2,Food3");
   }

   @Test
    void testGetAllFoodEntriesByProteinContentEmptyCollection() {
       Collection<FoodEntry> expected = new ArrayList<>();

       assertIterableEquals(expected, dailyFoodDiary.getAllFoodEntriesByProteinContent(),
               "Expected empty collection");

   }

    @Test
    void testGetAllFoodEntriesByProteinContentExistingFoodEntriesUnmodifiable() throws UnknownFoodException {
        List<FoodEntry> expected= new ArrayList<>();
        FoodEntry food1 = new FoodEntry("Food1",1.1,
                new NutritionInfo(20,30,50));
        FoodEntry food2 = new FoodEntry("Food2",2.2,
                new NutritionInfo(30,50,20));
        FoodEntry food3 = new FoodEntry("Food3",3.3,
                new NutritionInfo(50,20,30));
        expected.add(food1);
        expected.add(food2);
        expected.add(food3);

        when(nutritionInfoApiMock.getNutritionInfo("Food1")).
                thenReturn(new NutritionInfo(20,30,50));
        when(nutritionInfoApiMock.getNutritionInfo("Food2")).
                thenReturn(new NutritionInfo(30,50,20));
        when(nutritionInfoApiMock.getNutritionInfo("Food3")).
                thenReturn(new NutritionInfo(50,20,30));

        dailyFoodDiary.addFood(Meal.DINNER,"Food1",1.1);
        dailyFoodDiary.addFood(Meal.DINNER,"Food2",2.2);
        dailyFoodDiary.addFood(Meal.DINNER,"Food3",3.3);

        List<FoodEntry> actual = dailyFoodDiary.getAllFoodEntriesByProteinContent();

        assertIterableEquals(expected,actual,
                "Expected sorted by protein content collection with 3 elements");
        assertThrows(UnsupportedOperationException.class,()->actual.add(food1),
                "method GetAllFoodEntriesByProteinContent must return unmodifiable copy");

    }

    @Test
    void testGetDailyCaloriesIntakeWhenNoFoodIsAdded() {
        assertEquals(0.0,dailyFoodDiary.getDailyCaloriesIntake(),
                "Expected 0 calories intake when no food has been added");
    }

    @Test
    void testGetDailyCaloriesIntakeWithDifferentFoods()throws UnknownFoodException {
        when(nutritionInfoApiMock.getNutritionInfo("meal1")).
                thenReturn(new NutritionInfo(20,30,50));
        when(nutritionInfoApiMock.getNutritionInfo("meal2")).
                thenReturn(new NutritionInfo(50,20,30));
        when(nutritionInfoApiMock.getNutritionInfo("meal3")).
                thenReturn(new NutritionInfo(30,10,60));
        when(nutritionInfoApiMock.getNutritionInfo("meal4")).
                thenReturn(new NutritionInfo(80,5,15));

        dailyFoodDiary.addFood(Meal.LUNCH,"meal1",1);
        dailyFoodDiary.addFood(Meal.LUNCH,"meal2",1.1);
        dailyFoodDiary.addFood(Meal.LUNCH,"meal3",2.2);
        dailyFoodDiary.addFood(Meal.LUNCH,"meal4",3.3);

        assertEquals(3492.5,dailyFoodDiary.getDailyCaloriesIntake(),
                "Expected 3492.5 calories, but "
                        + dailyFoodDiary.getDailyCaloriesIntakePerMeal(Meal.LUNCH)+" was returned ");

    }

    @Test
    void testGetDailyCaloriesIntakePerMealWhenMealIisNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                ()->dailyFoodDiary.getDailyCaloriesIntakePerMeal(null),
             "Meal cannot be null,expected exception to be thrown");
    }

    @Test
    void testGetDailyCaloriesIntakePerMealWhenNoFoodIsAdded() {
        assertEquals(0.0,dailyFoodDiary.getDailyCaloriesIntakePerMeal(Meal.DINNER),
                "Expected 0 calories intake when no food has been added");
    }

    @Test
    void testGetDailyCaloriesIntakePerMealWhenNoFoodIsAddedForDinner() throws UnknownFoodException {
        when(nutritionInfoApiMock.getNutritionInfo("breakfast")).
                thenReturn(new NutritionInfo(20,30,50));

        dailyFoodDiary.addFood(Meal.BREAKFAST,"breakfast",2.3);

        assertEquals(0.0,dailyFoodDiary.getDailyCaloriesIntakePerMeal(Meal.DINNER),
                "Expected 0 calories intake when no food has been added for dinner");
    }

    @Test
    void testGetDailyCaloriesIntakePerMealWithDifferentFoods() throws UnknownFoodException {
        when(nutritionInfoApiMock.getNutritionInfo("meal1")).
                thenReturn(new NutritionInfo(20,30,50));
        when(nutritionInfoApiMock.getNutritionInfo("meal2")).
                thenReturn(new NutritionInfo(50,20,30));
        when(nutritionInfoApiMock.getNutritionInfo("meal3")).
                thenReturn(new NutritionInfo(30,10,60));
        when(nutritionInfoApiMock.getNutritionInfo("meal4")).
                thenReturn(new NutritionInfo(80,5,15));

        dailyFoodDiary.addFood(Meal.BREAKFAST,"meal1",1);
        dailyFoodDiary.addFood(Meal.LUNCH,"meal2",1.1);
        dailyFoodDiary.addFood(Meal.LUNCH,"meal3",2.2);
        dailyFoodDiary.addFood(Meal.LUNCH,"meal4",3.3);

        assertEquals(2942.5,dailyFoodDiary.getDailyCaloriesIntakePerMeal(Meal.LUNCH),
                "Expected 2942.5 calories for lunch , but " +
                dailyFoodDiary.getDailyCaloriesIntakePerMeal(Meal.LUNCH)+ " was returned ");
    }


}
