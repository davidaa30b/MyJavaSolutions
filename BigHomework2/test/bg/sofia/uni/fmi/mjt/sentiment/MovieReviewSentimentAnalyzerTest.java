package bg.sofia.uni.fmi.mjt.sentiment;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class
MovieReviewSentimentAnalyzerTest {

    private static final String TEMP_DATASET_NAME = "movieReviewsTemp";
    private static final String TEMP_STOP_WORDS_NAME = "stopwords";
    private static final String SUFFIX_FILES = ".txt";
    private List<Review> reviewList;
    private List<String> stopWordsList;
    private MovieReviewSentimentAnalyzer analyzer = setUpTestingData();

    public MovieReviewSentimentAnalyzerTest() throws IOException {
    }

    @Test
    void testIfExistingDatasetIsLoadedCorrectly() {
        final int expected = reviewList.size() + stopWordsList.size();
        final int actual = analyzer.getAllReviews().size() + analyzer.getAllStopWords().size();
        assertEquals(expected, actual);
    }

    @Test
    void testGetReviewSentimentWithExistingInFileNormalText() {
        final String actualReview = "Even fans of Ismail Merchant's work by me , I suspect , would have a hard time sitting through this one film .";
        final double EXPECTED_VALUE =  1.3051948051948052727272727272727;
        assertEquals(EXPECTED_VALUE, analyzer.getReviewSentiment(actualReview));
    }

    @Test
    void testGetReviewSentimentWithExistingInFileCheckIfSameFromDifferentFormat() {
        final String normalReview = "Even fans of Ismail Merchant's work , I suspect , would have a hard time sitting through this one .";
        final String fancyReview = "EVEn FAns of ismail MerchANt's work , I SUSPECT , would HaVE a hard time SiTTing ThrouGH this oNE .";
        assertEquals(analyzer.getReviewSentiment(fancyReview), analyzer.getReviewSentiment(normalReview));
    }

    @Test
    void testGetReviewSentimentWithUserTextUnknown() {
        final String actualReview = "I had a stroke from this movie. It was awful !";
        //System.out.println(analyzer.getReviewSentiment(actualReview));
        final double EXPECTED_VALUE = -1.0;
        assertEquals(EXPECTED_VALUE, analyzer.getReviewSentiment(actualReview));
    }

    @Test
    void testGetReviewSentimentWithUserTextKnown() {
        final String actualReview = "Bloody Sunday has the grace to meaty subject and drawn engaging endless assault of embarrassingly.";
        final double EXPECTED_VALUE = 3.111111111111111;
        assertEquals(EXPECTED_VALUE, analyzer.getReviewSentiment(actualReview));
    }

    @Test
    void testGetReviewSentimentAsNamePositive() {
        final String positiveReview = "Year undergoing zingers quiet making oscar worth !";
        //System.out.println(analyzer.getReviewSentiment(positiveReview));
        final String EXPECTED_VALUE = "positive";
        //System.out.println(analyzer.getReviewSentimentAsName(positiveReview));
        assertEquals(EXPECTED_VALUE, analyzer.getReviewSentimentAsName(positiveReview));
    }

    @Test
    void testGetReviewSentimentAsNameSomewhatPositive() {
        final String positiveReview = "characters are I think bad making path shell !";
        //System.out.println(analyzer.getReviewSentiment(positiveReview));
        final String EXPECTED_VALUE = "somewhat positive";
        //System.out.println(analyzer.getReviewSentimentAsName(positiveReview));
        assertEquals(EXPECTED_VALUE, analyzer.getReviewSentimentAsName(positiveReview));
    }
    @Test
    void testGetReviewSentimentAsNameNeutral() {
        final String positiveReview = "movie's StorY is good like everything makes it strong !";
        //System.out.println(analyzer.getReviewSentiment(positiveReview));
        final String EXPECTED_VALUE = "neutral";
        //System.out.println(analyzer.getReviewSentimentAsName(positiveReview));
        assertEquals(EXPECTED_VALUE, analyzer.getReviewSentimentAsName(positiveReview));
    }

    @Test
    void testGetReviewSentimentAsNameSomewhatNegative() {
        final String positiveReview = "Youth is much goose . Watching attempts keeps convolutions freakish ";
        //System.out.println(analyzer.getReviewSentiment(positiveReview));
        final String EXPECTED_VALUE = "somewhat negative";
        //System.out.println(analyzer.getReviewSentimentAsName(positiveReview));
        assertEquals(EXPECTED_VALUE, analyzer.getReviewSentimentAsName(positiveReview));
    }

    @Test
    void testGetReviewSentimentAsNameNegative() {
        final String positiveReview = "Aims are waste detailing o'fallon 60 cats";
        //System.out.println(analyzer.getReviewSentiment(positiveReview));
        final String EXPECTED_VALUE = "negative";
        //System.out.println(analyzer.getReviewSentimentAsName(positiveReview));
        assertEquals(EXPECTED_VALUE, analyzer.getReviewSentimentAsName(positiveReview));
    }

    @Test
    void testGetReviewSentimentAsNameUnknown() {
        final String positiveReview = "Cats have small paws ! ";
        //System.out.println(analyzer.getReviewSentiment(positiveReview));
        final String EXPECTED_VALUE = "unknown";
        //System.out.println(analyzer.getReviewSentimentAsName(positiveReview));
        assertEquals(EXPECTED_VALUE, analyzer.getReviewSentimentAsName(positiveReview));
    }

    @Test
    void testGetWordSentimentCorrect() {
        final String testWord = "like";
        final double EXPECTED_VALUE = 2.125;
        assertEquals(EXPECTED_VALUE, analyzer.getWordSentiment(testWord));
    }

    @Test
    void testGetWordSentimentCorrectStrangeFormat() {
        final String testWord = "LiKe";
        final double EXPECTED_VALUE = 2.125;
        assertEquals(EXPECTED_VALUE, analyzer.getWordSentiment(testWord));
    }

    @Test
    void testGetWordSentimentStopWord() {
        final String testWord = "at";
        final double EXPECTED_VALUE = -1.0;
        assertEquals(EXPECTED_VALUE, analyzer.getWordSentiment(testWord));
    }


    @Test
    void testGetWordSentimentNonexistentFromDataset() {
        final String testWord = "cat";
        final double EXPECTED_VALUE = -1.0;
        assertEquals(EXPECTED_VALUE, analyzer.getWordSentiment(testWord));
    }

    @Test
    void testGetWordSentimentNotWord() {
        final String testWord = "-like!*";
        final double EXPECTED_VALUE = -1.0;
        assertEquals(EXPECTED_VALUE, analyzer.getWordSentiment(testWord));
    }

    @Test
    void testGetWordFrequencyCorrect() {
        final String testWord = "like";
        final double EXPECTED_VALUE = 9;
        assertEquals(EXPECTED_VALUE, analyzer.getWordFrequency(testWord));
    }

    @Test
    void testGetWordFrequencyCorrectDiffFormat() {
        final String testWord = "FiLM";
        final double EXPECTED_VALUE = 7;
        assertEquals(EXPECTED_VALUE, analyzer.getWordFrequency(testWord));
    }
    @Test
    void testGetWordFrequencyIncorrect() {
        final String testWord = "at";
        final double EXPECTED_VALUE = 0;
        assertEquals(EXPECTED_VALUE, analyzer.getWordFrequency(testWord));
    }

    @Test
    void testGetMostFrequentWordsIncorrect() {
        final int LIMIT = -1;
        assertThrows (IllegalArgumentException.class , () ->analyzer.getMostFrequentWords(LIMIT),
                "Error ! N can only be positive ! ");
    }

    @Test
    void testGetMostFrequentWordCorrect() {
        final int LIMIT = 10;
        List<String> expected = Arrays.asList("like" , "film" , "one" , "good" , "movie" , "us" , "movies" , "going" , "much" , "characters");

        List<String> actual = analyzer.getMostFrequentWords(LIMIT);

        assertIterableEquals(actual,expected);
    }

    @Test
    void testGetMostPositiveWordsIncorrect() {
        final int LIMIT = -1;
        assertThrows (IllegalArgumentException.class , () ->analyzer.getMostPositiveWords(LIMIT),
                "Error ! N can only be positive ! ");

    }

    @Test
    void testGetMostPositiveWordsCorrect() {
        final int LIMIT = 10;
        List<String> expected = Arrays.asList("year" , "undergoing" , "zingers" , "prevention" , "meaty" , "size" , "jones" , "independent" , "shiver" , "quiet");

        List<String> actual = analyzer.getMostPositiveWords(LIMIT);

        assertIterableEquals(actual,expected);
    }

    @Test
    void testGetMostNegativeWordsIncorrect() {
        final int LIMIT = -1;
        assertThrows (IllegalArgumentException.class , () ->analyzer.getMostNegativeWords(LIMIT),
                "Error ! N can only be positive ! ");

    }

    @Test
    void testGetMostNegativeWordsCorrect() {
        final int LIMIT = 10;
        List<String> expected = Arrays.asList("visual" , "downright" , "rewrite" , "comes" , "plausible" , "transparent" , "indulgent" , "sex" , "script's" , "endless");

        List<String> actual = analyzer.getMostNegativeWords(LIMIT);

        assertIterableEquals(actual,expected);
    }

    @Test
    void testAppendReviewIncorrectSentimentScore() {
        final String reviewTest = "That is the review";
        final int sentimentScore = 5;
        assertThrows (IllegalArgumentException.class , () ->analyzer.appendReview(reviewTest,sentimentScore) ,
                "Error ! The sentiment score must be in the [0:4] range ! ");
    }

    @Test
    void testAppendReviewIncorrectReviewText() {
        final String reviewTest = "";
        final int sentimentScore = 2;
        assertThrows (IllegalArgumentException.class , () ->analyzer.appendReview(reviewTest,sentimentScore) ,
                "Error ! The review can not be blank , empty , or null ");
    }

    @Test
    void testAppendReviewCorrect() {
        final String reviewTest = "This film was not bad nor good either ! I like Michael but Trevor goes in too much for a 40-year-old ! ";
        final int sentimentScore = 2;

        assertTrue(analyzer.appendReview(reviewTest, sentimentScore));
    }
    @Test
    void testAppendReviewCorrectUpdatedDataset() {
        final String reviewTest = "This film was not bad nor good either ! I like Michael but Trevor goes in too much for a 40-year-old ! ";
        final int sentimentScore = 2;
        final int BEFORE = analyzer.getAllReviews().size();
        final int ADDITION = 1;
        analyzer.appendReview(reviewTest, sentimentScore);
        int AFTER = analyzer.getAllReviews().size();

        assertEquals(BEFORE + ADDITION, AFTER);
    }

    @Test
    void testAppendReviewCorrectUpdatedDatasetFrequency() {
        final String testWord = "liKe";
        final String reviewTest = "This film was " + testWord + " not bad nor good either ! I  " + testWord + "  Michael but Trevor goes in too much for a 40-year-old ! ";
        final double BEFORE = analyzer.getWordFrequency(testWord);
        final int ADDITION = 2;
        final int sentimentScore = 2;
        analyzer.appendReview(reviewTest, sentimentScore);
        final double AFTER = analyzer.getWordFrequency(testWord);
        System.out.println("BEFORE = " + BEFORE + " ,AFTER " + AFTER );
        assertEquals(BEFORE + ADDITION, AFTER);
    }

    @Test
    void testAppendReviewCorrectUpdatedDatasetSentiment() {
        final String testWord = "Like";
        final String reviewTest = "This film was " + testWord + " not bad nor good either ! I  " + testWord + "  Michael but Trevor goes in too much for a 40-year-old ! ";

        final double BEFORE = analyzer.getWordTotalSentiment(testWord);
        final int sentimentScore = 2;

        analyzer.appendReview(reviewTest, sentimentScore);
        final double AFTER = analyzer.getWordTotalSentiment(testWord);
        assertEquals(BEFORE + sentimentScore, AFTER);

    }

    @Test
    void testGetSentimentDictionarySize() {
        final int EXPECTED = 545;
        assertEquals(EXPECTED, analyzer.getSentimentDictionarySize());
    }

    @Test
    void testIsStopWord () {
        final int FIRST = 1;
        String toTest = stopWordsList.get(FIRST);
        assertTrue(analyzer.isStopWord(toTest));
    }




    private MovieReviewSentimentAnalyzer setUpTestingData() throws IOException {
        Reader reviewsInStream = movieReviewsStream();
        Reader stopWordsInStream = stopWordsStream();
        Writer reviewsOut = new FileWriter(TEMP_DATASET_NAME+SUFFIX_FILES,true);

        try (BufferedReader reader = new BufferedReader(reviewsInStream)) {
            reviewList = reader.lines()
                    .map(Review::of)
                    .collect(Collectors.toList());
        }

        try (BufferedReader reader = new BufferedReader(stopWordsInStream)) {
            stopWordsList = reader.lines()
                    .toList();
        }

        return new MovieReviewSentimentAnalyzer(stopWordsStream(),movieReviewsStream(),reviewsOut);
    }


    private static Reader movieReviewsStream() throws IOException {
        Path tempFile = Files.createTempFile(TEMP_DATASET_NAME, SUFFIX_FILES );

        List<String> content = Arrays.asList(
                "1 A series of escapades demonstrating the adage that what is good for the goose is also good for the gander , some of which occasionally amuses but none of which amounts to much of a story .",
                "4 This quiet , introspective and entertaining independent is worth seeking .",
                "1 Even fans of Ismail Merchant's work , I suspect , would have a hard time sitting through this one .",
                "3 A positively thrilling combination of ethnography and all the intrigue , betrayal , deceit and murder of a Shakespearean tragedy or a juicy soap opera .",
                "1 Aggressive self-glorification and a manipulative whitewash .",
                "4 A comedy-drama of nearly epic proportions rooted in a sincere performance by the title character undergoing midlife crisis .",
                "1 Narratively , Trouble Every Day is a plodding mess .",
                "3 The Importance of Being Earnest , so thick with wit it plays like a reading from Bartlett's Familiar Quotations",
                "1 But it doesn't leave you with much .",
                "1 You could hate it for the same reason .",
                "1 There's little to recommend Snow Dogs , unless one considers cliched dialogue and perverse escapism a source of high hilarity .",
                "1 Kung Pow is Oedekerk's realization of his childhood dream to be in a martial-arts flick , and proves that sometimes the dreams of youth should remain just that .",
                "4 The performances are an absolute joy .",
                "3 Fresnadillo has something serious to say about the ways in which extravagant chance can distort our perspective and throw us off the path of good sense .",
                "3 I still like Moonlight Mile , better judgment be damned .",
                "3 A welcome relief from baseball movies that try too hard to be mythic , this one is a sweet and modest and ultimately winning story .",
                "3 a bilingual charmer , just like the woman who inspired it",
                "2 Like a less dizzily gorgeous companion to Mr. Wong's In the Mood for Love -- very much a Hong Kong movie despite its mainland setting .",
                "1 As inept as big-screen remakes of The Avengers and The Wild Wild West .",
                "2 It's everything you'd expect -- but nothing more .",
                "4 Best indie of the year , so far .",
                "3 Hatfield and Hicks make the oddest of couples , and in this sense the movie becomes a study of the gambles of the publishing world , offering a case study that exists apart from all the movie's political ramifications .",
                "1 It's like going to a house party and watching the host defend himself against a frothing ex-girlfriend .",
                "2 That the Chuck Norris `` grenade gag  occurs about 7 times during Windtalkers is a good indication of how serious-minded the film is .",
                "2 The plot is romantic comedy boilerplate from start to finish .",
                "2 It arrives with an impeccable pedigree , mongrel pep , and almost indecipherable plot complications .",
                "2 A film that clearly means to preach exclusively to the converted .",
                "1 While The Importance of Being Earnest offers opportunities for occasional smiles and chuckles , it doesn't give us a reason to be in the theater beyond Wilde's wit and the actors ' performances .",
                "1 The latest vapid actor's exercise to appropriate the structure of Arthur Schnitzler's Reigen .",
                "2 More vaudeville show than well-constructed narrative , but on those terms it's inoffensive and actually rather sweet .",
                "2 Nothing more than a run-of-the-mill action flick .",
                "0 Hampered -- no , paralyzed -- by a self-indulgent script ... that aims for poetry and ends up sounding like satire .",
                "2 Ice Age is the first computer-generated feature cartoon to feel like other movies , and that makes for some glacial pacing early on .",
                "2 There's very little sense to what's going on here , but the makers serve up the cliches with considerable dash .",
                "2 Cattaneo should have followed the runaway success of his first film , The Full Monty , with something different .",
                "1 They're the unnamed , easily substitutable forces that serve as whatever terror the heroes of horror movies try to avoid .",
                "1 It almost feels as if the movie is more interested in entertaining itself than in amusing us .",
                "0 The movie's progression into rambling incoherence gives new meaning to the phrase ` fatal script error . '",
                "3 Don't judge this one too soon - it's a dark , gritty story but it takes off in totally unexpected directions and keeps on going .",
                "3 So could young romantics out on a date .",
                "0 Tartakovsky's team has some freakish powers of visual charm , but the five writers slip into the modern rut of narrative banality .",
                "3 Vincent Gallo is right at home in this French shocker playing his usual bad boy weirdo role .",
                "4 If a horror movie's primary goal is to frighten and disturb , then They works spectacularly well ... A shiver-inducing , nerve-rattling ride .",
                "1 This 100-minute movie only has about 25 minutes of decent material .",
                "2 Fortunately , you still have that option .",
                "3 Less the sensational true-crime hell-jaunt purists might like like and more experimental in its storytelling ( though no less horrifying for it ) .",
                "3 As tricky and satisfying as any of David Mamet's airless cinematic shell games .",
                "1 I'm sure the filmmaker would disagree , but , honestly , I don't see the point .",
                "4 Jones has tackled a meaty subject and drawn engaging characters while peppering the pages with memorable zingers .",
                "4 Bloody Sunday has the grace to call for prevention rather than to place blame , making it one of the best war movies ever made .",
                "1 Takes a clunky TV-movie approach to detailing a chapter in the life of the celebrated Irish playwright , poet and drinker .",
                "2 Finally coming down off of Miramax's deep shelves after a couple of aborted attempts , Waking Up in Reno makes a strong case for letting sleeping dogs lie .",
                "0 Thanks largely to Williams , all the interesting developments are processed in 60 minutes -- the rest is just an overexposed waste of film .",
                "0 Comes across as a relic from a bygone era , and its convolutions ... feel silly rather than plausible .",
                "4 Perhaps it's cliche to call the film ` refreshing , ' but it is .",
                "4 Both lead performances are Oscar-size .",
                "0 Run for your lives !",
                "3 Mark Pellington's latest pop thriller is as kooky and overeager as it is spooky and subtly in love with myth .",
                "3 Claude Chabrol's camera has a way of gently swaying back and forth as it cradles its characters , veiling tension beneath otherwise tender movements .",
                "3 Transforms one of ( Shakespeare's ) deepest tragedies into a smart new comedy .",
                "1 The screenplay by James Eric , James Horton and director Peter O'Fallon ... is so pat it makes your teeth hurt .",
                "3 But arriving at a particularly dark moment in history , it offers flickering reminders of the ties that bind us .",
                "1 Its generic villains lack any intrigue ( other than their funny accents ) and the action scenes are poorly delivered .",
                "3 The characters are more deeply thought through than in most ` right-thinking ' films .",
                "3 The film , despite the gratuitous cinematic distractions impressed upon it , is still good fun .",
                "0 Downright transparent is the script's endless assault of embarrassingly ham-fisted sex jokes that reek of a script rewrite designed to garner the film a `` cooler  PG-13 rating ."
                );

        Files.write(tempFile, content, StandardOpenOption.APPEND);

        String[] info = Files.readString(tempFile).split(System.lineSeparator());

        return new StringReader(Arrays.stream(info).collect(Collectors.joining(System.lineSeparator())));
    }

    private static Reader stopWordsStream() throws IOException {

        Path tempFile = Files.createTempFile(TEMP_STOP_WORDS_NAME, SUFFIX_FILES);

        List<String> content = Arrays.asList(
                "a",
                "about",
                "after",
                "again",
                "against",
                "all",
                "am",
                "an",
                "and",
                "any",
                "are",
                "aren't",
                "as",
                "at",
                "be",
                "because",
                "been",
                "before",
                "being",
                "below",
                "between",
                "both",
                "but",
                "by",
                "can't",
                "cannot",
                "could",
                "couldn't",
                "did",
                "didn't",
                "do",
                "does",
                "doesn't",
                "doing",
                "don't",
                "down",
                "during",
                "each",
                "few",
                "for",
                "from",
                "further",
                "had",
                "hadn't",
                "has",
                "hasn't",
                "have",
                "haven't",
                "having",
                "he",
                "he'd",
                "he'll",
                "he's",
                "her",
                "here",
                "here's",
                "hers",
                "herself",
                "him",
                "himself",
                "his",
                "how",
                "how's",
                "i",
                "i'd",
                "i'll",
                "i'm",
                "i've",
                "if",
                "in",
                "into",
                "is",
                "isn't",
                "it",
                "it's",
                "its",
                "itself",
                "let's",
                "me",
                "more",
                "most",
                "mustn't",
                "my",
                "myself",
                "no",
                "nor",
                "not",
                "of",
                "off",
                "on",
                "once",
                "only",
                "or",
                "other",
                "ought",
                "our",
                "ours",
                "ourselves",
                "out",
                "over",
                "own",
                "same",
                "shan't",
                "she",
                "she'd",
                "she'll",
                "she's",
                "should",
                "shouldn't",
                "so",
                "some",
                "such",
                "than",
                "that",
                "that's",
                "the",
                "their",
                "theirs",
                "them",
                "themselves",
                "then",
                "there",
                "there's",
                "these",
                "they",
                "they'd",
                "they'll",
                "they're",
                "they've",
                "this",
                "those",
                "through",
                "to",
                "too",
                "under",
                "until",
                "up",
                "very",
                "was",
                "wasn't",
                "we",
                "we'd",
                "we'll",
                "we're",
                "we've",
                "were",
                "weren't",
                "what",
                "what's",
                "when",
                "when's",
                "where",
                "where's",
                "which",
                "while",
                "who",
                "who's",
                "whom",
                "why",
                "why's",
                "with",
                "won't",
                "would",
                "wouldn't",
                "you",
                "you'd",
                "you'll",
                "you're",
                "you've",
                "your",
                "yours",
                "yourself",
                "yourselves"
        );


        Files.write(tempFile, content, StandardOpenOption.CREATE);

        String[] info = Files.readString(tempFile).split(System.lineSeparator());

        return new StringReader(Arrays.stream(info).collect(Collectors.joining(System.lineSeparator())));
    }







}
