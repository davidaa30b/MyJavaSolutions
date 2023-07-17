package bg.sofia.uni.fmi.mjt.news.wrapper;

import bg.sofia.uni.fmi.mjt.news.articles.TopHeadLinesArticles;
import bg.sofia.uni.fmi.mjt.news.exceptions.ApiKeyInvalidException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsAPIWrapperException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ParameterInvalidException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ParametersMissingException;
import bg.sofia.uni.fmi.mjt.news.exceptions.RateLimitedException;

public interface NewsAPIWrapper {
    TopHeadLinesArticles getArticlesBySpecifier(
            String[] keywords, String country, String category, int page, int pageSize)
            throws NewsAPIWrapperException, RateLimitedException, ParameterInvalidException,
            ApiKeyInvalidException, ParametersMissingException, ApiKeyMissingException;

}
