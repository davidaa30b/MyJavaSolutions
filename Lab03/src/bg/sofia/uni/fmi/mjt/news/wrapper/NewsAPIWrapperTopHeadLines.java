package bg.sofia.uni.fmi.mjt.news.wrapper;

import bg.sofia.uni.fmi.mjt.news.articles.TopHeadLinesArticles;
import com.google.gson.Gson;
import bg.sofia.uni.fmi.mjt.news.exceptions.ApiKeyInvalidException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsAPIWrapperException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ParameterInvalidException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ParametersMissingException;
import bg.sofia.uni.fmi.mjt.news.exceptions.RateLimitedException;
import bg.sofia.uni.fmi.mjt.news.specifiers.MyUri;
import bg.sofia.uni.fmi.mjt.news.utils.Validations;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NewsAPIWrapperTopHeadLines implements NewsAPIWrapper {
    private static final String API_KEY = "enter-your-api-key-here";
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "newsapi.org";
    private static final String API_ENDPOINT_PATH = "/v2/top-headlines";
    private static final Gson GSON = new Gson();

    private final HttpClient newsHttpClient;
    private String apiKey;
    public static NewsAPIWrapperTopHeadLines getInstance(HttpClient newsHttpClient) throws ApiKeyMissingException {
        return new NewsAPIWrapperTopHeadLines(newsHttpClient);
    }

    public static NewsAPIWrapperTopHeadLines getInstance(HttpClient newsHttpClient, String apiKey) throws ApiKeyMissingException {
        return new NewsAPIWrapperTopHeadLines(newsHttpClient, apiKey);
    }

    private NewsAPIWrapperTopHeadLines(HttpClient newsHttpClient) throws ApiKeyMissingException {
        this(newsHttpClient, API_KEY);
    }

    private NewsAPIWrapperTopHeadLines(HttpClient newsHttpClient, String apiKey) throws ApiKeyMissingException {
        this.newsHttpClient = newsHttpClient;
        if (!Validations.correctString(apiKey)) {
            throw new ApiKeyMissingException("Api key missing exception thrown ! Please specify your Api key ! ");
        }
        this.apiKey = apiKey;
    }
    @Override
    public TopHeadLinesArticles getArticlesBySpecifier(

            String[] keywords, String country, String category, int page, int pageSize)
            throws NewsAPIWrapperException, RateLimitedException, ParameterInvalidException,
            ApiKeyInvalidException, ParametersMissingException {

        HttpResponse<String> response ;

        try {
            URI uri = MyUri.builder(apiKey, API_ENDPOINT_SCHEME, API_ENDPOINT_HOST,
                    API_ENDPOINT_PATH, keywords, category, country, page, pageSize).newBuilder();

            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            response = newsHttpClient.send(request, HttpResponse.BodyHandlers.ofString());

        }
        catch (ParameterInvalidException e) {
            throw new ParameterInvalidException(e.getLocalizedMessage());
        }
        catch (ParametersMissingException e) {
            throw new ParametersMissingException(e.getLocalizedMessage());
        }
        catch (Exception e) {
            throw new NewsAPIWrapperException(" Could not load any news from news api service! ", e);
        }


        return Validations.getResponseCode(response, GSON);

    }
}
