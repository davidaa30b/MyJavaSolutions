package bg.sofia.uni.fmi.mjt.news.articles;

import java.util.Arrays;

public record TopHeadLinesArticles(String status, int totalResults, NewsArticle[] articles) {

    @Override
    public boolean equals(Object obj) {

        TopHeadLinesArticles other = (TopHeadLinesArticles) obj;

        return this.status.equals(other.status) &&
                this.totalResults == other.totalResults &&
                Arrays.equals(this.articles, other.articles);
    }


}
