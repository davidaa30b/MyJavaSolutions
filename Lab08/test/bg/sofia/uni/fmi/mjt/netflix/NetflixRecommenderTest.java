package bg.sofia.uni.fmi.mjt.netflix;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.mjt.netflix.ContentType.MOVIE;
import static bg.sofia.uni.fmi.mjt.netflix.ContentType.SHOW;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class NetflixRecommenderTest {
    private static List<Content> netflixContent;
    private static NetflixRecommender recommender;


    @Test
     void testIfExistingDatasetIsLoadedCorrectly() throws IOException {
        Reader tracksStream = initTracksStream();

        try (BufferedReader reader = new BufferedReader(tracksStream)) {
            netflixContent = reader.lines()
                    .skip(1)
                    .peek(System.out::println)
                    .map(Content::of)
                    .collect(Collectors.toList());
        }
        recommender = new NetflixRecommender(initTracksStream());

        int expected = netflixContent.size();
        int actual = recommender.getAllContent().size();
        assertEquals(expected, actual);
    }

    @Test
    void testGetAllGenres() throws IOException {

        Reader tracksStream = initTracksStream();

        try (BufferedReader reader = new BufferedReader(tracksStream)) {
            netflixContent = reader.lines()
                    .skip(1)
                    .map(Content::of)
                    .collect(Collectors.toList());
        }
        recommender = new NetflixRecommender(initTracksStream());

        List<String> genresExpected = new ArrayList<>();
        genresExpected.add("drama");
        genresExpected.add("romance");
        genresExpected.add("comedy");
        genresExpected.add("european");
        genresExpected.add("documentation");
        genresExpected.add("action");
        genresExpected.add("war");
        genresExpected.add("crime");
        genresExpected.add("thriller");
        genresExpected.add("western");

        assertEquals(recommender.getAllGenres(),genresExpected);

    }

    @Test
    void testGetLongestMovie() throws IOException {
        Reader tracksStream = initTracksStream();

        try (BufferedReader reader = new BufferedReader(tracksStream)) {
            netflixContent = reader.lines()
                    .skip(1)
                    .map(Content::of)
                    .collect(Collectors.toList());
        }
        recommender = new NetflixRecommender(initTracksStream());

        List<String> expectedGenres = new ArrayList<>();
        expectedGenres.add("action");
        expectedGenres.add("drama");
        expectedGenres.add("war");

        Content expected = new Content("tm44204","The Guns of Navarone",MOVIE,
                "A team of allied saboteurs are assigned an impossible mission:" +
                        " infiltrate an impregnable Nazi-held island and destroy the two " +
                        "enormous long-range field guns that prevent the rescue of 2;000 trapped " +
                        "British soldiers.",1961,158,expectedGenres,-1,
                "tt0054953",7.5,50748.0);
        assertEquals(expected,recommender.getTheLongestMovie());
    }

    @Test
    void testGroupContentByType() throws IOException {
        Reader tracksStream = initTracksStream();

        try (BufferedReader reader = new BufferedReader(tracksStream)) {
            netflixContent = reader.lines()
                    .skip(1)
                    .map(Content::of)
                    .collect(Collectors.toList());
        }
        recommender = new NetflixRecommender(initTracksStream());
        Map<ContentType, Set<Content>> map = new HashMap<>();
        map = netflixContent.stream()
                .collect(Collectors.groupingBy(
                        Content::type, Collectors.toSet()));

        assertEquals(map,recommender.groupContentByType());
    }

    @Test
    void testGetTopNRatedContent() throws IOException {
        Reader tracksStream = initTracksStream();

        try (BufferedReader reader = new BufferedReader(tracksStream)) {
            netflixContent = reader.lines()
                    .skip(1)
                    .map(Content::of)
                    .collect(Collectors.toList());
        }
        recommender = new NetflixRecommender(initTracksStream());

        final int expectedN = 2;
        List<String>expectedGenres1 = new ArrayList<>();
        expectedGenres1.add("comedy");
        expectedGenres1.add("european");
        List<String>expectedGenres2 = new ArrayList<>();
        expectedGenres1.add("drama");
        expectedGenres1.add("crime");

        List<Content>expectedList = new ArrayList<>();
        Content content1 = new Content("ts22164","Monty Python's Flying Circus",SHOW,
                "A British sketch comedy series with the shows being composed of surreality; " +
                        "risqué or innuendo-laden humour; sight gags and observational sketches without " +
                        "punchlines.",
                1969,30,expectedGenres1,1,"tt0063929",8.8,
                73424.0);

        Content content2 = new Content("tm84618","Taxi Driver",MOVIE,
                "A mentally unstable Vietnam War veteran works as a night-time taxi driver in " +
                        "New York City where the perceived decadence and sleaze feed his urge for violent " +
                        "action.",1976,114,expectedGenres2,-1,"tt0075314",8.2,808582.0);

        expectedList.add(content1);
        expectedList.add(content2);

        assertIterableEquals(expectedList,recommender.getTopNRatedContent(expectedN));
    }

    @Test
    void testGetTopNRatedContentNZero() throws IOException {

        Reader tracksStream = initTracksStream();

        try (BufferedReader reader = new BufferedReader(tracksStream)) {
            netflixContent = reader.lines()
                    .skip(1)
                    .map(Content::of)
                    .collect(Collectors.toList());
        }
        recommender = new NetflixRecommender(initTracksStream());
    final int expectedN = 0;
    List<Content>expectedList = new ArrayList<>();

    assertEquals(expectedList,recommender.getTopNRatedContent(expectedN));
    }

    @Test
    void testGetTopNRatedContentNException() throws IOException {
        Reader tracksStream = initTracksStream();

        try (BufferedReader reader = new BufferedReader(tracksStream)) {
            netflixContent = reader.lines()
                    .skip(1)
                    .map(Content::of)
                    .collect(Collectors.toList());
        }
        recommender = new NetflixRecommender(initTracksStream());
        assertThrows(IllegalArgumentException.class,()->recommender.getTopNRatedContent(-1),
                "Error ! Invalid input for number of top movies . ");
    }

    @Test
    void testGetSimilarContent() throws IOException {
        Reader tracksStream = initTracksStream();

        try (BufferedReader reader = new BufferedReader(tracksStream)) {
            netflixContent = reader.lines()
                    .skip(1)
                    .map(Content::of)
                    .collect(Collectors.toList());
        }
        recommender = new NetflixRecommender(initTracksStream());        List<String>expectedGenres1 = new ArrayList<>();
        expectedGenres1.add("comedy");
        expectedGenres1.add("european");
        Content content1 = new Content("ts22164","Monty Python's Flying Circus",SHOW,
                "A British sketch comedy series with the shows being composed of surreality; " +
                        "risqué or innuendo-laden humour; sight gags and observational sketches without " +
                        "punchlines.",
                1969,30,expectedGenres1,1,"tt0063929",8.8,
                73424.0);

        System.out.println(recommender.getSimilarContent(content1));

    }

    @Test
    void testGetContentByKeywords() throws IOException {
        Reader tracksStream = initTracksStream();

        try (BufferedReader reader = new BufferedReader(tracksStream)) {
            netflixContent = reader.lines()
                    .skip(1)
                    .map(Content::of)
                    .collect(Collectors.toList());
        }
        recommender = new NetflixRecommender(initTracksStream());        String[] keywords = {"mentally"};
        System.out.println(recommender.getContentByKeywords(keywords));
    }





    public static Reader initTracksStream() {
        String[] tracks = {
                "id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes",
                "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama';'crime'],-1,tt0075314,8.2,808582.0",
                "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama';'action';'thriller';'european'],-1,tt0068473,7.7,107673.0",
                "tm120801,The Dirty Dozen,MOVIE,12 American military prisoners in World War II are ordered to infiltrate a well-guarded enemy château and kill the Nazi officers vacationing there. The soldiers; most of whom are facing death sentences for a variety of violent crimes; agree to the mission and the possible commuting of their sentences.,1967,150,['war';'action'],-1,tt0061578,7.7,72662.0",
                "ts22164,Monty Python's Flying Circus,SHOW,A British sketch comedy series with the shows being composed of surreality; risqué or innuendo-laden humour; sight gags and observational sketches without punchlines.,1969,30,['comedy';'european'],1,tt0063929,8.8,73424.0",
                "tm70993,Life of Brian,MOVIE,Brian Cohen is an average young Jewish man; but through a series of ridiculous events; he gains a reputation as the Messiah. When he's not dodging his followers or being scolded by his shrill mother; the hapless Brian has to contend with the pompous Pontius Pilate and acronym-obsessed members of a separatist movement. Rife with Monty Python's signature absurdity; the tale finds Brian's life paralleling Biblical lore; albeit with many more laughs.,1979,94,['comedy'],-1,tt0079470,8.0,395024.0",
                "tm14873,Dirty Harry,MOVIE,When a madman dubbed 'Scorpio' terrorizes San Francisco; hard-nosed cop; Harry Callahan – famous for his take-no-prisoners approach to law enforcement – is tasked with hunting down the psychopath. Harry eventually collars Scorpio in the process of rescuing a kidnap victim; only to see him walk on technicalities. Now; the maverick detective is determined to nail the maniac himself.,1971,102,['thriller'; 'action'; 'crime'],-1,tt0066999,7.7,155051.0",
                "tm119281,Bonnie and Clyde,MOVIE,In the 1930s; bored waitress Bonnie Parker falls in love with an ex-con named Clyde Barrow and together they start a violent crime spree through the country; stealing cars and robbing banks.,1967,110,['crime'; 'drama'; 'action'],-1,tt0061418,7.7,112048.0",
                "tm98978,The Blue Lagoon,MOVIE,Two small children and a ship's cook survive a shipwreck and find safety on an idyllic tropical island. Soon; however; the cook dies and the young boy and girl are left on their own. Days become years and Emmeline and Richard make a home for themselves surrounded by exotic creatures and nature's beauty. But will they ever see civilization again?,1980,104,['romance';'action';'drama'],-1,tt0080453,5.8,69844.0",
                "tm44204,The Guns of Navarone,MOVIE,A team of allied saboteurs are assigned an impossible mission: infiltrate an impregnable Nazi-held island and destroy the two enormous long-range field guns that prevent the rescue of 2;000 trapped British soldiers.,1961,158,['action';'drama';'war'],-1,tt0054953,7.5,50748.0",
                "tm67378,The Professionals,MOVIE,An arrogant Texas millionaire hires four adventurers to rescue his kidnapped wife from a notorious Mexican bandit.,1966,117,['western'; 'action'; 'european'],-1,tt0060862,7.3,16446.0",
                "tm69997,Richard Pryor: Live in Concert,MOVIE,Richard Pryor delivers monologues on race; sex; family and his favorite target—himself; live at the Terrace Theatre in Long Beach; California.,1979,78,['comedy'; 'documentation'],-1,tt0079807,8.1,5141.0",
                "tm16479,White Christmas,MOVIE,Two talented song-and-dance men team up after the war to become one of the hottest acts in show business. In time they befriend and become romantically involved with the beautiful Haynes sisters who comprise a sister act.,1954,115,['romance';'comedy'],-1,tt0047673,7.5,42488.0",
                "tm135083,Cairo Station,MOVIE,Qinawi; a physically challenged peddler who makes his living selling newspapers in the central Cairo train station; is obsessed by Hanuma; an attractive young woman who sells drinks. While she jokes with him about a possible relationship; she is actually in love with Abu Siri; a strong and respected porter at the station who is struggling to unionize his fellow workers to combat their boss' exploitative and abusive treatment.,1958,77,['drama';'crime';'comedy'],-1,tt0051390,7.5,4471.0"
        };

        return new StringReader(Arrays.stream(tracks).collect(Collectors.joining(System.lineSeparator())));
    }


}
