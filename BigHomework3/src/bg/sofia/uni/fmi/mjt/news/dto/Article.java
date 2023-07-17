package bg.sofia.uni.fmi.mjt.news.dto;

public class Article {
    private String status;
    private String source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishAt;
    private String content;

    public Article(String source, String author, String title, String description,
                   String url, String urlToImage, String publishAt, String content) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishAt = publishAt;
        this.content = content;
    }

}
