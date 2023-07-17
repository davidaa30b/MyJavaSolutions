package bg.sofia.uni.fmi.mjt.news.util;

public class RequestOptionsValidations {

    private static final int MAX_PAGES = 3;
    private static final int MIN_PAGES = 0;

    public static boolean checkCountryInvalid(String country) {
        return !new SortingOptions().countryTypesSet.contains(country);
    }

    public static boolean checkCategoryInvalid(String category) {
        return !new SortingOptions().categoryTypesSet.contains(category);
    }

    public static boolean checkLanguageInvalid(String language) {
        return !new SortingOptions().languageTypesSet.contains(language);
    }

    public static boolean checkRelevanceInvalid(String relevance) {
        return !new SortingOptions().relevanceTypesSet.contains(relevance);
    }

    public static boolean checkPageSizeInvalid(int pageSize) {
        return pageSize <= MIN_PAGES || pageSize > MAX_PAGES;
    }




}
