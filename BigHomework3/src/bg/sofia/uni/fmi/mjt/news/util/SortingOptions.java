package bg.sofia.uni.fmi.mjt.news.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SortingOptions {
    private String[] relevanceStringList  = { "relevancy", "popularity", "publishedAt"};
    public final Set<String> relevanceTypesSet = new HashSet(Arrays.asList(relevanceStringList));

    private String[] categoryStringList = {"business", "entertainment", "general", "health", "science", "sports" , "technology"};

    public final Set<String> categoryTypesSet = new HashSet<>(Arrays.asList(categoryStringList));

    private String[] languageStringList = {"ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt", "ru" , "ud" , "zh", "se"};

    public final Set<String> languageTypesSet = new HashSet<>(Arrays.asList(languageStringList));

    private String[] countryStringList = {"za", "ve", "us", "ua", "tw",
        "tr", "th", "sk", "si", "sg", "se", "sa", "ru", "rs",
        "ro", "pt", "pl", "ph", "nz", "no", "nl", "ng", "my",
        "mx", "ma", "lv", "lt", "kr", "jp", "it", "in", "il",
        "ie", "id", "hu", "hk", "gr", "gb", "eg", "fr", "de",
        "cz", "cu", "co", "cn", "ch", "ca", "br", "bg", "be",
        "au", "at", "ae", "ar"};

    public final Set<String> countryTypesSet = new HashSet<>(Arrays.asList(countryStringList));
}
