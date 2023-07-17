package bg.sofia.uni.fmi.mjt.news.articles;

import bg.sofia.uni.fmi.mjt.news.articles.Source;
import bg.sofia.uni.fmi.mjt.news.utils.Validations;

public record NewsArticle(Source source, String author, String title,
                          String description, String url, String urlToImage,
                          String publishedAt, String content) {

    @Override
    public boolean equals(Object obj) {
        NewsArticle other = (NewsArticle) obj;
        return other.source.equals(this.source)  &&
                (Validations.areTwoObjNull(other.author, this.author) ||
                        (other.author.equals(this.author))) &&
                (Validations.areTwoObjNull(other.title, this.title) ||
                        (other.title.equals(this.title))) &&
                (Validations.areTwoObjNull(other.description, this.description) ||
                        (other.description.equals(this.description))) &&
                (Validations.areTwoObjNull(other.url, this.url) ||
                        (other.url.equals(this.url))) &&
                (Validations.areTwoObjNull(other.urlToImage, this.urlToImage) ||
                        (other.urlToImage.equals(this.urlToImage))) &&
                (Validations.areTwoObjNull(other.publishedAt, this.publishedAt) ||
                        (other.publishedAt.equals(this.publishedAt))) &&
                (Validations.areTwoObjNull(other.content, this.content) ||
                        (other.content.equals(this.content))) ;
    }
}
