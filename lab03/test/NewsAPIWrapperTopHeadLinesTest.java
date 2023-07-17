package bg.sofia.uni.fmi.mjt.news;

import bg.sofia.uni.fmi.mjt.news.articles.NewsArticle;
import bg.sofia.uni.fmi.mjt.news.articles.Source;
import bg.sofia.uni.fmi.mjt.news.articles.TopHeadLinesArticles;
import com.google.gson.Gson;
import bg.sofia.uni.fmi.mjt.news.exceptions.ApiKeyInvalidException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsAPIWrapperException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ParameterInvalidException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ParametersMissingException;
import bg.sofia.uni.fmi.mjt.news.exceptions.RateLimitedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import bg.sofia.uni.fmi.mjt.news.wrapper.NewsAPIWrapperTopHeadLines;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

public class NewsAPIWrapperTopHeadLinesTest {
    private static TopHeadLinesArticles headLinesArticles;
    private static String headLinesArticlesJson;

    private static TopHeadLinesArticles headLinesArticlesWithCategory;

    private static String headLinesArticlesWithCategoryJson;

    private static TopHeadLinesArticles headLinesArticlesWithTheThreeParams;

    private static String headLinesArticlesWithTheThreeParamsJson;

    private static String[] keywords;

    private static String[] keywordsForThreeParams;
    @Mock
    private HttpClient newsHttpClientMock;
    @Mock
    private HttpResponse<String> httpNewsResponseMock;

    private NewsAPIWrapperTopHeadLines client;

    @BeforeAll
    public static void setUpClass() {
        List<String> list = new ArrayList<>();
        list.add("car");
        list.add("us");
        String[] arr = new String[list.size()];
        arr = list.toArray(arr);
        keywords = arr;

        Source source = new Source("fox news", "Fox News");
        NewsArticle article = new NewsArticle(source,
                "Fox News",
                "House oversight committee is a place for the ‘bomb throwers’ in Congress: Susan Ferrechio | Fox News Video",
                "Political analyst Ameshia Cross and Washington Times political reporter Susan Ferrechio react to Kevin McCarthy getting scorched by the media for putting certain Conservative individuals on the House oversight committee.",
                "\"https://video.foxnews.com/v/6319090663112/",
                "https://a57.foxnews.com/cf-images.us-east-1.prod.boltdns.net/v1/static/694940094001/6a77e8ba-7274-4e8f-82c7-d52b371bf5cc/3e38aaf1-ad5b-420b-ac85-e6a1e70147d2/1280x720/match/1024/512/image.jpg?ve=1&tl=1",
                "2023-01-22T17:37:23.2481171Z",
                "©2023 FOX News Network, LLC. All rights reserved. This material may not be published, broadcast, rewritten, or redistributed. All market data delayed 20 minutes.");
        headLinesArticles = new TopHeadLinesArticles("ok", 23, new NewsArticle[] {article});
        headLinesArticlesJson = new Gson().toJson(headLinesArticles);

        Source source2 = new Source(null, "YouTube");
        NewsArticle article2 = new NewsArticle(source2,
                null,
                "Airam Blanco, cuando un sentimiento no entiende de edades - FaroTV Ceuta",
                "Redacción y locución: Carlos DíazImagen y edición: Marina RiscoPuedes leer el reportaje completo aquí: https://elfarodeceuta.es/Suscríbete a nuestro canal ht...",
                "https://www.youtube.com/watch?v=RvPwN0urDkE",
                "https://i.ytimg.com/vi/RvPwN0urDkE/maxresdefault.jpg",
                "2023-01-22T17:27:16Z",
                null);
        headLinesArticlesWithCategory = new TopHeadLinesArticles("ok",20,new NewsArticle[]{article2} );
        headLinesArticlesWithCategoryJson= new Gson().toJson(headLinesArticlesWithCategory);

        List<String> list3 = new ArrayList<>();
        list3.add("nba");
        String[] arr3 = new String[list3.size()];
        keywordsForThreeParams = list3.toArray(arr3);

        Source source3 = new Source(null, "YouTube");
        NewsArticle article3 = new NewsArticle(source3,
                null,
                "76ERS at KINGS | FULL GAME HIGHLIGHTS | January 21, 2023 - NBA",
                "Never miss a moment with the latest news, trending stories and highlights to bring you closer to your favorite players and teams.Download now: https://app.li...",
                "https://www.youtube.com/watch?v=gP2P-6ztt34",
                "https://i.ytimg.com/vi/gP2P-6ztt34/maxresdefault.jpg",
                "2023-01-22T05:53:25Z",
                null);
        headLinesArticlesWithTheThreeParams = new TopHeadLinesArticles("ok",6,new NewsArticle[]{article3} );
        headLinesArticlesWithTheThreeParamsJson = new Gson().toJson(headLinesArticlesWithTheThreeParams);

    }

    @BeforeEach
    public void setUp () throws ApiKeyMissingException, IOException, InterruptedException {
        when(newsHttpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpNewsResponseMock);

        client = NewsAPIWrapperTopHeadLines.getInstance(newsHttpClientMock);
    }

    @Test
    void testMissingApiKeyExceptionNull () {
        assertThrows(ApiKeyMissingException.class,() ->client = NewsAPIWrapperTopHeadLines.getInstance(newsHttpClientMock, null));
    }

    @Test
    void testMissingApiKeyExceptionEmpty () {
        assertThrows(ApiKeyMissingException.class,() ->client = NewsAPIWrapperTopHeadLines.getInstance(newsHttpClientMock, ""));
    }

    @Test
    void testMissingApiKeyExceptionBlank () {
        assertThrows(ApiKeyMissingException.class,() ->client = NewsAPIWrapperTopHeadLines.getInstance(newsHttpClientMock, " "));
    }
    @Test
    void testInvalidParametersForPagePositive() throws ApiKeyMissingException {
        final int EXCEEDING_PAGE = 4;
        final int RIGHT_PAGE_SIZE = 10;
        assertThrows(ParameterInvalidException.class,() -> client.getArticlesBySpecifier(keywords , null, null, EXCEEDING_PAGE , RIGHT_PAGE_SIZE));
    }

    @Test
    void testInvalidParametersForPageNegative() {
        final int EXCEEDING_PAGE = -1;
        final int RIGHT_PAGE_SIZE = 10;
        assertThrows(ParameterInvalidException.class,() -> client.getArticlesBySpecifier(keywords , null, null, EXCEEDING_PAGE , RIGHT_PAGE_SIZE));
    }

    @Test
    void testInvalidParametersForPageSizePositive() {
        final int RIGHT_PAGE = 2;
        final int EXCEEDING_PAGE_SIZE = 51;
        assertThrows(ParameterInvalidException.class,() -> client.getArticlesBySpecifier(keywords , null, null, RIGHT_PAGE , EXCEEDING_PAGE_SIZE));
    }

    @Test
    void testInvalidParametersForPageSizeNegative() {
        final int RIGHT_PAGE = 3;
        final int EXCEEDING_PAGE_SIZE = -10;
        assertThrows(ParameterInvalidException.class,() -> client.getArticlesBySpecifier(keywords , null, null, RIGHT_PAGE , EXCEEDING_PAGE_SIZE));
    }

    @Test
    void testInvalidParametersForBothPageAndPageSize() {
        final int EXCEEDING_PAGE = 50;
        final int EXCEEDING_PAGE_SIZE = -10;
        assertThrows(ParameterInvalidException.class,() -> client.getArticlesBySpecifier(keywords , null, null, EXCEEDING_PAGE , EXCEEDING_PAGE_SIZE));
    }

    @Test
    void testParametersMissingForKeyWords() {
        final int RIGHT_PAGE = 3;
        final int RIGHT_PAGE_SIZE = 10;
        final String[] emptyString = {} ;
        assertThrows(ParametersMissingException.class,() -> client.getArticlesBySpecifier(emptyString , null, null, RIGHT_PAGE , RIGHT_PAGE_SIZE));
    }

    @Test
    void testParametersMissingForKeyWordsButHasOthers() {
        final int RIGHT_PAGE = 3;
        final int RIGHT_PAGE_SIZE = 10;
        final String[] emptyString = {} ;
        final String COUNTRY = "us";
        final String CATEGORY = "business";
        assertThrows(ParametersMissingException.class,() -> client.getArticlesBySpecifier(emptyString , COUNTRY, CATEGORY, RIGHT_PAGE , RIGHT_PAGE_SIZE));
    }

    @Test
    public void testGetArticlesBySpecifierIsWrapped() throws Exception {
        final int RIGHT_PAGE = 3;
        final int RIGHT_PAGE_SIZE = 10;
        IOException expectedExc = new IOException();
        when(newsHttpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenThrow(expectedExc);

        try {
            client.getArticlesBySpecifier(keywords , null, null, RIGHT_PAGE , RIGHT_PAGE_SIZE);
        } catch (Exception actualExc) {
            assertEquals(expectedExc, actualExc.getCause());
        }
    }

    @Test
    public void testGetArticlesBySpecifierHttpBadReq() throws Exception {
        when(httpNewsResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        final int RIGHT_PAGE = 3;
        final int RIGHT_PAGE_SIZE = 10;
        try {
            client.getArticlesBySpecifier(keywords , null, null, RIGHT_PAGE , RIGHT_PAGE_SIZE);
        } catch (Exception e) {
            assertEquals(NewsAPIWrapperException.class, e.getClass());
        }
    }

    @Test
    public void testGetArticlesBySpecifierHttpUnauthorized() throws Exception {
        when(httpNewsResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);
        final int RIGHT_PAGE = 3;
        final int RIGHT_PAGE_SIZE = 10;
        try {
            client.getArticlesBySpecifier(keywords , null, null, RIGHT_PAGE , RIGHT_PAGE_SIZE);
        } catch (Exception e) {
            assertEquals(ApiKeyInvalidException.class, e.getClass());
        }
    }

    @Test
    public void testGetArticlesBySpecifierHttpToManyReq() throws Exception {
        final int HTTP_TO0_MANY_REQ = 429;
        when(httpNewsResponseMock.statusCode()).thenReturn(HTTP_TO0_MANY_REQ);
        final int RIGHT_PAGE = 3;
        final int RIGHT_PAGE_SIZE = 10;
        try {
            client.getArticlesBySpecifier(keywords , null, null, RIGHT_PAGE , RIGHT_PAGE_SIZE);
        } catch (Exception e) {
            assertEquals( RateLimitedException.class, e.getClass());
        }
    }
    @Test
    public void testGetArticlesBySpecifierHttpServerError() throws Exception {
        when(httpNewsResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_SERVER_ERROR);
        final int RIGHT_PAGE = 3;
        final int RIGHT_PAGE_SIZE = 10;
        try {
            client.getArticlesBySpecifier(keywords , null, null, RIGHT_PAGE , RIGHT_PAGE_SIZE);
        } catch (Exception e) {
            assertEquals( NewsAPIWrapperException.class, e.getClass());
        }
    }

    @Test
    public void testGetArticlesBySpecifierValidOnlyKeywords()
            throws NewsAPIWrapperException, ParameterInvalidException,
            ApiKeyInvalidException, RateLimitedException, ParametersMissingException {

        when(httpNewsResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpNewsResponseMock.body()).thenReturn(headLinesArticlesJson);

        final int RIGHT_PAGE = 3;
        final int RIGHT_PAGE_SIZE = 1;
        var result =  client.getArticlesBySpecifier(keywords , null, null, RIGHT_PAGE , RIGHT_PAGE_SIZE);

        assertEquals(headLinesArticles, result);
    }

    @Test
    public void testGetArticlesBySpecifierValidAndCategory()
            throws NewsAPIWrapperException, ParameterInvalidException,
            ApiKeyInvalidException, RateLimitedException, ParametersMissingException {

        when(httpNewsResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpNewsResponseMock.body()).thenReturn(headLinesArticlesWithCategoryJson);

        final int RIGHT_PAGE = 3;
        final int RIGHT_PAGE_SIZE = 1;
        final String CATEGORY = "business";
        var result =  client.getArticlesBySpecifier(keywords , null, CATEGORY, RIGHT_PAGE , RIGHT_PAGE_SIZE);

        assertEquals(headLinesArticlesWithCategory, result);
    }

    @Test
    public void testGetArticlesBySpecifierValidForThreeParams()
            throws NewsAPIWrapperException, ParameterInvalidException,
            ApiKeyInvalidException, RateLimitedException, ParametersMissingException {

        when(httpNewsResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpNewsResponseMock.body()).thenReturn(headLinesArticlesWithTheThreeParamsJson);
        final int RIGHT_PAGE = 2;
        final int RIGHT_PAGE_SIZE = 1;
        final String CATEGORY = "sports";
        final String COUNTRY = "us";
        var result =  client.getArticlesBySpecifier(keywordsForThreeParams , COUNTRY, CATEGORY, RIGHT_PAGE , RIGHT_PAGE_SIZE);
        assertEquals(headLinesArticlesWithTheThreeParams, result);
    }









}
