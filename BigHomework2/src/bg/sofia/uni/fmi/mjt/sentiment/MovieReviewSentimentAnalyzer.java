package bg.sofia.uni.fmi.mjt.sentiment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.sentiment.SentimentTypes.NEGATIVE;
import static bg.sofia.uni.fmi.mjt.sentiment.SentimentTypes.NEUTRAL;
import static bg.sofia.uni.fmi.mjt.sentiment.SentimentTypes.POSITIVE;
import static bg.sofia.uni.fmi.mjt.sentiment.SentimentTypes.SOMEWHAT_NEGATIVE;
import static bg.sofia.uni.fmi.mjt.sentiment.SentimentTypes.SOMEWHAT_POSITIVE;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;

public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {

    private static final int MIN_LENGTH_WORD = 2;
    private static final double INVALID_SENTIMENT = -1.0;
    private static final int ONE_INT = 1;
    private static final int ONE_DOUBLE = 1;
    private static final String UNKNOWN = "unknown";
    private final Writer reviewsOut;
    private List<Review> reviewsList = new ArrayList<>();
    private final Set<String> stopWordsList = new HashSet<>();
    private Map<String, Integer> allWordsWithFreq = new HashMap<>();
    private Map<String, Sentiment> allWordsWithSentiment = new HashMap<>();
    private void readReviews(Reader reviewsIn) {
        try (BufferedReader reader = new BufferedReader(reviewsIn)) {

            reviewsList = reader.lines()
                    .map(Review::of)
                    .toList();

        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load dataset", e);
        }
    }

    private void readStopWords(Reader stopwordsIn) {
        try (BufferedReader buffer = new BufferedReader(stopwordsIn)) {
            String line;

            while ((line = buffer.readLine()) != null) {
                this.stopWordsList.add(line.toLowerCase());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateDataset (Review review) {
        String[] curContent = review.content().split("[-\\s]");
        for (String word : curContent) {
            if (validWordsFromFile(word)) {
                Integer freq = allWordsWithFreq.get(word.toLowerCase());
                Sentiment sentiment = allWordsWithSentiment.get(word.toLowerCase());

                if (freq == null) {
                    allWordsWithFreq.put(word.toLowerCase(), ONE_INT);
                } else {
                    allWordsWithFreq.put(word.toLowerCase(), freq + 1);
                }

                if (sentiment == null) {
                    allWordsWithSentiment.put(word.toLowerCase(), new Sentiment(review.rating(), ONE_DOUBLE));
                    allWordsWithSentiment.get(word.toLowerCase()).setFlag(false);
                } else if (sentiment.getFlag()) {
                    sentiment.increaseSentiment(review.rating());
                    sentiment.increaseTimeFound(ONE_DOUBLE);
                    allWordsWithSentiment.get(word.toLowerCase()).setFlag(false);
                }
            }
        }
        for (String word : curContent) {
            if (validWordsFromFile(word)) {
                allWordsWithSentiment.get(word.toLowerCase()).setFlag(true);
            }
        }
    }

    private void fillAllTheWordsFromFile() {
        reviewsList.forEach(review -> { updateDataset(review);
        });
    }


    private boolean validWordsFromFile(String word) {

        if (word.length() < MIN_LENGTH_WORD) {
            return false;
        }

        if (!word.matches("^[a-zA-Z0-9']*$")) {
            return false;
        }

        return !isStopWord(word);
    }

    private boolean wordExistsInFile(String word) {
        return allWordsWithFreq.containsKey(word.toLowerCase());
    }

    private boolean stringValid(String str) {
        return !str.isEmpty() && !str.isBlank() && str != null;
    }

    private boolean validWordFromUser(String word) {

        return wordExistsInFile(word) && validWordsFromFile(word) && stringValid(word);
    }

    public List<Review> getAllReviews() {
        return reviewsList;
    }

    public Set<String> getAllStopWords() {
        return Collections.unmodifiableSet(stopWordsList);
    }


    public MovieReviewSentimentAnalyzer(Reader stopwordsIn, Reader reviewsIn, Writer reviewsOut) {
        readStopWords(stopwordsIn);
        readReviews(reviewsIn);
        this.reviewsOut = reviewsOut;
        fillAllTheWordsFromFile();
    }


    @Override
    public double getReviewSentiment(String review) {
        double sentimentScore = 0.0;
        double wordCounter = 0.0;
        String[] wordsInReview = review.split(" ");

        for (String wordToCheck : wordsInReview) {
            if (validWordFromUser(wordToCheck)) {
                System.out.println(wordToCheck + " has score : " + getWordSentiment(wordToCheck));
                sentimentScore += getWordSentiment(wordToCheck);
                //System.out.println(wordToCheck + " has made score to be " + sentimentScore);
                wordCounter++;
            }
        }


        System.out.println("Total sentiment score is : " + sentimentScore + " and total word count is : " + wordCounter);
        double score = sentimentScore / wordCounter;

        return  ( score >= NEGATIVE.getLeftRange() && score <= POSITIVE.getRightRange()) ? score : -1.0;
    }

    @Override
    public String getReviewSentimentAsName(String review) {
        double sentimentScore = getReviewSentiment(review);

        if (sentimentScore >= NEGATIVE.getLeftRange() && sentimentScore < NEGATIVE.getRightRange()) {
            return NEGATIVE.getType();
        }

        if (sentimentScore >= SOMEWHAT_NEGATIVE.getLeftRange() && sentimentScore < SOMEWHAT_NEGATIVE.getRightRange()) {
            return SOMEWHAT_NEGATIVE.getType();
        }

        if (sentimentScore >= NEUTRAL.getLeftRange() && sentimentScore < NEUTRAL.getRightRange()) {
            return NEUTRAL.getType();
        }

        if (sentimentScore >= SOMEWHAT_POSITIVE.getLeftRange() && sentimentScore < SOMEWHAT_POSITIVE.getRightRange()) {
            return SOMEWHAT_POSITIVE.getType();
        }

        if (sentimentScore >= POSITIVE.getLeftRange() && sentimentScore <= POSITIVE.getRightRange()) {
            return POSITIVE.getType();
        }

        return UNKNOWN;
    }

    public double getWordTotalSentiment(String word) {
        if (!validWordFromUser(word)) {
            return INVALID_SENTIMENT;
        }

        return allWordsWithSentiment.get(word.toLowerCase()).getTotalSentiment();
    }
    @Override
    public double getWordSentiment(String word) {

        if (!validWordFromUser(word)) {
            return INVALID_SENTIMENT;
        }
        //System.out.print(word + " has a score of : " +  allWordsWithSentiment.get(word.toLowerCase()).getAverage() + " and  ");
        //allWordsWithSentiment.get(word.toLowerCase()).showCurrInfo();

        return allWordsWithSentiment.get(word.toLowerCase()).getAverage();

    }

    @Override
    public int getWordFrequency(String word) {
        int wordFrequency = 0;

        if (!validWordFromUser(word)) {
            return 0;
        }

        if ( allWordsWithFreq.containsKey(word.toLowerCase())) {
            wordFrequency = allWordsWithFreq.get(word.toLowerCase());
        }

        return wordFrequency;
    }

    @Override
    public List<String> getMostFrequentWords(int n) {
        if ( n < 0 ) {
            throw new IllegalArgumentException();
        }

        allWordsWithFreq = allWordsWithFreq
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect( toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));

        /*for (Map.Entry<String, Integer> entry : allWordsWithFreq.entrySet())
            System.out.println("Word = " + entry.getKey() +
                    ", Freq = " + entry.getValue());*/

        return allWordsWithFreq
                .keySet()
                .stream()
                .limit(n)
                .peek(System.out::println)
                .toList();
    }

    @Override
    public List<String> getMostPositiveWords(int n) {
        if ( n < 0 ) {
            throw new IllegalArgumentException();
        }

        allWordsWithSentiment = allWordsWithSentiment
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));

        /*for (Map.Entry<String, Sentiment> entry : allWordsWithSentiment.entrySet())
            System.out.println("Word = " + entry.getKey() +
                    ", Average sentiment = " + entry.getValue().getAverage());*/

        return allWordsWithSentiment
                .keySet()
                .stream()
                .limit(n)
                .peek(System.out::println)
                .toList();
    }

    @Override
    public List<String> getMostNegativeWords(int n) {
        if ( n < 0 ) {
            throw new IllegalArgumentException();
        }

        allWordsWithSentiment = allWordsWithSentiment
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));

        /*for (Map.Entry<String, Sentiment> entry : allWordsWithSentiment.entrySet())
            System.out.println("Word = " + entry.getKey() +
                    ", Average sentiment = " + entry.getValue().getAverage());*/

        return allWordsWithSentiment
                .keySet()
                .stream()
                .limit(n)
                .peek(System.out::println)
                .toList();
    }

    @Override
    public boolean appendReview(String review, int sentiment)  {


        if (!stringValid(review) || sentiment < NEGATIVE.getLeftRange() || sentiment > POSITIVE.getRightRange()) {
            throw new IllegalArgumentException();
        }
        String newData = sentiment + " " + review + System.lineSeparator();

        try {
            reviewsOut.write(newData);
            reviewsOut.close();

            //datasetUpdate
            reviewsList = reviewsList.stream().collect(toCollection(ArrayList::new));
            reviewsList.add(Review.of(newData));
            updateDataset(Review.of(newData));
            return true;
        }
        catch (IOException e) {
            return false;
        }


    }

    @Override
    public int getSentimentDictionarySize() {
        return allWordsWithSentiment.size();
    }

    @Override
    public boolean isStopWord(String word) {
        return stopWordsList.contains(word.toLowerCase());
    }


}
