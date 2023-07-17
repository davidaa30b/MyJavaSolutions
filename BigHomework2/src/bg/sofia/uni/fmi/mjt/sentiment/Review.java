package bg.sofia.uni.fmi.mjt.sentiment;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Review(int rating, String content) {

    private static final int RATING = 0;
    private static final int CONTEXT_BEGIN_INDEX = 2;
    private static final char TO_GET_NUM = '0';

    public static Review of(String line) {

        int rating = line.charAt(RATING) - TO_GET_NUM;
        String content = line.substring(CONTEXT_BEGIN_INDEX);

        return new Review(rating, content.toLowerCase());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review that = (Review) o;
        return Objects.equals(rating, that.rating) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }


}
