package bg.sofia.uni.fmi.mjt.netflix;

import javax.print.DocFlavor;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bg.sofia.uni.fmi.mjt.netflix.ContentType.MOVIE;

public class NetflixRecommender {

    private final static int M = 10000;
    private final static double MINIMAL_SCORE = 0;

    private List<Content> netflixContent;

    /**
     * Loads the dataset from the given {@code reader}.
     *
     * @param reader Reader from which the dataset can be read.
     */
    public NetflixRecommender(Reader reader) {
        try (var readerInput = new BufferedReader(reader)) {

            netflixContent = readerInput.lines()
                    .skip(1)
                    .map(Content::of)
                            .toList();

            System.out.println("Netflix content loaded: " + netflixContent.size());

        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load dataset", e);
        }
    }

    /**
     * Returns all movies and shows from the dataset in undefined order as an unmodifiable List.
     * If the dataset is empty, returns an empty List.
     *
     * @return the list of all movies and shows.
     */
    public List<Content> getAllContent() {
        return Collections.unmodifiableList(netflixContent);
    }

    /**
     * Returns a list of all unique genres of movies and shows in the dataset in undefined order.
     * If the dataset is empty, returns an empty List.
     *
     * @return the list of all genres
     */
    public List<String> getAllGenres() {

        Set<String> allGenres = new HashSet<>() ;

        for (var c: netflixContent
             ) {
            allGenres.addAll(c.genres());
        }
        Set<String> distinct = new HashSet<>(allGenres);
        List<String> list = new ArrayList<>();
        list.addAll(distinct);
        return list;
    }

    /**
     * Returns the movie with the longest duration / run time. If there are two or more movies
     * with equal maximum run time, returns any of them. Shows in the dataset are not considered by this method.
     *
     * @return the movie with the longest run time
     * @throws NoSuchElementException in case there are no movies in the dataset.
     */
    public Content getTheLongestMovie() {
        return netflixContent.stream()
                .filter(content -> content.type().equals(MOVIE))
                .max(Comparator.comparingInt(Content::runtime))
                .orElseThrow(()->new NoSuchElementException("The are no movies in Netflix"));
    }

    /**
     * Returns a breakdown of content by type (movie or show).
     *
     * @return a Map with key: a ContentType and value: the set of movies or shows on the dataset, in undefined order.
     */
    public Map<ContentType, Set<Content>> groupContentByType() {
        return netflixContent.stream()
               .collect(Collectors.groupingBy(
                      Content::type, Collectors.toSet()));
    }

    /**
     * Returns the top N movies and shows sorted by weighed IMDB rating in descending order.
     * If there are fewer movies and shows than {@code n} in the dataset, return all of them.
     * If {@code n} is zero, returns an empty list.
     *
     * The weighed rating is calculated by the following formula:
     * Weighted Rating (WR) = (v ÷ (v + m)) × R + (m ÷ (v + m)) × C
     * where
     * R is the content's own average rating across all votes. If it has no votes, its R is 0.
     * C is the average rating of content across the dataset
     * v is the number of votes for a content
     * m is a tunable parameter: sensitivity threshold. In our algorithm, it's a constant equal to 10_000.
     *
     * Check https://stackoverflow.com/questions/1411199/what-is-a-better-way-to-sort-by-a-5-star-rating for details.
     *
     * @param n the number of the top-rated movies and shows to return
     * @return the list of the top-rated movies and shows
     * @throws IllegalArgumentException if {@code n} is negative.
     */
    public List<Content> getTopNRatedContent(int n) {

        if (n == 0) {
            return Collections.emptyList();
        }

        if (n < 0) {
            throw new IllegalArgumentException("Number of tracks should be a non-negative number");
        }

        final double averageRating = netflixContent.stream().mapToDouble(Content::imdbScore).average().orElse(0.0);

        return netflixContent.stream()
                .sorted(Comparator.comparingDouble(content-> {
                    double votesCount = content.imdbVotes();
                    double imdbScore = content.imdbScore();
                    return -((votesCount / ( votesCount + M )) * imdbScore + ( M / (votesCount + M)) * averageRating);
                })).limit(n).toList();
    }

    /**
     * Returns a list of content similar to the specified one sorted by similarity is descending order.
     * Two contents are considered similar, only if they are of the same type (movie or show).
     * The used measure of similarity is the number of genres two contents share.
     * If two contents have equal number of common genres with the specified one, their mutual oder
     * in the result is undefined.
     *
     * @param content the specified movie or show.
     * @return the sorted list of content similar to the specified one.
     */
    public List<Content> getSimilarContent(Content content) {
        return netflixContent.stream().filter(contentOriginal -> contentOriginal.type()
                .equals(content.type())).sorted(Comparator.comparing(
                        contentOriginal-> {
                            return contentOriginal.genres().size() <=
                        content.genres().size();
                        }
        )).toList();
    }

    /**
     * Searches content by keywords in the description (case-insensitive).
     *
     * @param keywords the keywords to search for
     * @return an unmodifiable set of movies and shows whose description contains all specified keywords.
     */
    public Set<Content> getContentByKeywords(String... keywords) {
        return netflixContent.stream().filter(content -> {
            for (String keyword: keywords) {
                if (!content.description().toLowerCase().contains(" " + keyword.toLowerCase()) &&
                    !content.description().toLowerCase().contains(keyword.toLowerCase() + " ")) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toUnmodifiableSet());
    }

}