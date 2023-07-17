package bg.sofia.uni.fmi.mjt.news.utils;

import bg.sofia.uni.fmi.mjt.news.articles.TopHeadLinesArticles;
import com.google.gson.Gson;
import bg.sofia.uni.fmi.mjt.news.exceptions.ApiKeyInvalidException;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsAPIWrapperException;
import bg.sofia.uni.fmi.mjt.news.exceptions.RateLimitedException;

import java.net.HttpURLConnection;
import java.net.http.HttpResponse;

public class Validations {
    public static final int MAX_PAGES = 3; //page

    private static final int NOT_NEGATIVE = 0;
    public static final int MAX_ARTICLES = 50; //pageSize
    private static final int HTTP_TO0_MANY_REQ = 429;

    public static <T> boolean areTwoObjNull(T first, T second ) {
        return first == null && second == null;
    }

    public static boolean correctString(String input) {
        return  !(input == null) && !input.isBlank() && !input.isEmpty() ;
    }

    public static boolean correctStringArray(String[] arr) {

        for (var word : arr) {
            if (!correctString(word)) {
                return false;
            }
        }

        return arr.length != 0;
    }

    public static boolean checkPageCorrect(int page, int pageSize) {
        return page >= NOT_NEGATIVE && page <= MAX_PAGES && pageSize >= NOT_NEGATIVE && pageSize <= MAX_ARTICLES;
    }

    public static TopHeadLinesArticles getResponseCode(
            HttpResponse<String> response, Gson gson)
            throws RateLimitedException, NewsAPIWrapperException, ApiKeyInvalidException {

        switch (response.statusCode()) {
            case HttpURLConnection.HTTP_OK -> {
                return gson.fromJson(response.body(), TopHeadLinesArticles.class);
            }

            case HttpURLConnection.HTTP_BAD_REQUEST -> throw new NewsAPIWrapperException("Bad request exception!");

            case HttpURLConnection.HTTP_UNAUTHORIZED -> throw new ApiKeyInvalidException("" +
                    "Api key invalid exception thrown exception! Please check if your api key is correct !");

            case HTTP_TO0_MANY_REQ -> throw new RateLimitedException("" +
                    "Rate limited exception thrown ! Please take a break and come back tomorrow ! ");

            case HttpURLConnection.HTTP_SERVER_ERROR -> throw new NewsAPIWrapperException("Server error exception!");

            default -> throw new NewsAPIWrapperException("Unexpected error exception thrown ! ");
        }


    }
}
