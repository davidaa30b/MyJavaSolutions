package bg.sofia.uni.fmi.mjt.netflix;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Content( String id, String title, ContentType type,
                      String description, int releaseYear, int runtime,
                      List<String> genres, int seasons, String imdbId,
                      double imdbScore, double imdbVotes) {



    private static final int ID = 0;
    private static final int TITLE = 1;
    private static final int TYPE = 2;
    private static final int DESCRIPTION = 3;
    private static final int RELEASE_YEAR = 4;
    private static final int RUNTIME = 5;
    private static final int GENRES = 6;
    private static final int SEASONS = 7;
    private static final int IMDB_ID = 8;
    private static final int IMDB_SCORE = 9;
    private static final int IMDB_VOTES = 10;
    private static final String PEAK_ATTRIBUTE_DELIMITER = ",";




    public static Content of(String line) {
        String[] tokens = line.split(PEAK_ATTRIBUTE_DELIMITER);

        String id = tokens[ID];
        String title = tokens[TITLE];
        ContentType type = ContentType.valueOf(tokens[TYPE]);
        String description = tokens[DESCRIPTION];
        int releaseYear = Integer.parseInt(tokens[RELEASE_YEAR]);
        int runtime = Integer.parseInt(tokens[RUNTIME]);
        List<String> genres = Stream.of(tokens[GENRES].split(";"))
                .map(s -> s.replace("[", "").replace("]", "").replaceAll("'", "").trim())
                .collect(Collectors.toList());
        int seasons = Integer.parseInt(tokens[SEASONS]);
        String imdbId = tokens[IMDB_ID];
        double imdbScore = Double.parseDouble(tokens[IMDB_SCORE]);
        double imdbVotes = Double.parseDouble(tokens[IMDB_VOTES]);

        return new Content(id, title, type, description, releaseYear, runtime,
                genres, seasons, imdbId, imdbScore, imdbVotes);
    }

    //public static List<String> get

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content that = (Content) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
