package bg.sofia.uni.fmi.mjt.news.requests;

import bg.sofia.uni.fmi.mjt.news.dto.Article;

import java.util.List;

public class SearchReport {
    private String status;
    private int totalResults;
    private List<Article> articleList;

    public int getArticlesCount() {
        return  articleList.size();
    }
}
