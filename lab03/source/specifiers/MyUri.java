package bg.sofia.uni.fmi.mjt.news.specifiers;

import bg.sofia.uni.fmi.mjt.news.exceptions.ParameterInvalidException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ParametersMissingException;
import bg.sofia.uni.fmi.mjt.news.utils.Validations;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

public class MyUri {
    private static final String CONCAT_SYMBOL = "&";
    private static final String EQUATION_SYMBOL = "=";
    private static final String PLUS_SYMBOL = "+";
    private static final String MUST_HAVE_QUERY_SYM = "q";
    private static final int FIRST_OBJECT = 0;



    //from here the info enters the builder
    public static MyUriBuilder builder(String apiKey, String apiEndPointScheme, String apiEndPointHost,
                                       String apiEndPointPath, String[] keywords,
                                       String category, String country, int page, int pageSize)
            throws ParametersMissingException, ParameterInvalidException {


        return new MyUriBuilder(apiKey, apiEndPointScheme, apiEndPointHost,
                apiEndPointPath, keywords,
                category, country, page, pageSize);
    }

    // Builder Class
    public static class MyUriBuilder {

        private final String apiKey;
        private final String apiEndPointScheme;
        private final String apiEndPointHost;
        private final String apiEndPointPath;
        private final int page;
        private final int pageSize;
        private String apiEndPointQuery = MUST_HAVE_QUERY_SYM + EQUATION_SYMBOL ;

        private final EnumMap<Specifiers, List<String>> optionsParameters; //keywords,country or category

        private void fillEntryQuery() {
            StringBuilder queryExpression = new StringBuilder();

            for (int i = 0 ; i < optionsParameters.get(Specifiers.KEYWORD).size(); i++ ) {
                queryExpression.append(optionsParameters.get(Specifiers.KEYWORD).get(i));

                if (i != optionsParameters.get(Specifiers.KEYWORD).size() - 1) {
                    queryExpression.append(PLUS_SYMBOL);
                }
            }


            if (optionsParameters.get(Specifiers.COUNTRY) != null) {
                queryExpression.append(CONCAT_SYMBOL).append(Specifiers.COUNTRY.getParameter()).append(EQUATION_SYMBOL);
                queryExpression.append(optionsParameters.get(Specifiers.COUNTRY).get(FIRST_OBJECT));

            }

            if (optionsParameters.get(Specifiers.CATEGORY) != null) {
                queryExpression.append(CONCAT_SYMBOL).append(
                        Specifiers.CATEGORY.getParameter()).append(EQUATION_SYMBOL);

                queryExpression.append(optionsParameters.get(Specifiers.CATEGORY).get(FIRST_OBJECT));
            }

            this.apiEndPointQuery += queryExpression;
            this.apiEndPointQuery += CONCAT_SYMBOL + Specifiers.PAGE.getParameter() + EQUATION_SYMBOL;
            this.apiEndPointQuery += page;
            this.apiEndPointQuery += CONCAT_SYMBOL + Specifiers.PAGE_SIZE.getParameter() + EQUATION_SYMBOL;
            this.apiEndPointQuery += pageSize;
            this.apiEndPointQuery += CONCAT_SYMBOL + Specifiers.API_KEY.getParameter() + EQUATION_SYMBOL;
            this.apiEndPointQuery += apiKey;
        }

        private MyUriBuilder(String apiKey, String apiEndPointScheme, String apiEndPointHost,
                             String apiEndPointPath, String[] keywords,
                             String category, String country, int page, int pageSize)
                throws ParametersMissingException, ParameterInvalidException {

            this.apiKey = apiKey;
            this.apiEndPointScheme = apiEndPointScheme;
            this.apiEndPointHost = apiEndPointHost;
            this.apiEndPointPath = apiEndPointPath;

            if (!Validations.checkPageCorrect(page, pageSize)) {
                throw new ParameterInvalidException(String.format(
                        "can not exceed %s number of pages and %s number of articles",
                        Validations.MAX_PAGES, Validations.MAX_ARTICLES));
            }

            this.page = page;
            this.pageSize = pageSize;
            this.optionsParameters = new EnumMap<>(Specifiers.class);



            if (Validations.correctStringArray(keywords)) {

                optionsParameters.put(Specifiers.KEYWORD, new ArrayList<>());
                optionsParameters.get(Specifiers.KEYWORD).addAll(Arrays.stream(keywords).toList());
            }

            else {
                throw new ParametersMissingException("Missing keywords exception ! Keywords must be specified ! ");
            }

            if (Validations.correctString(category)) { // if null we do not add to request
                optionsParameters.put(Specifiers.CATEGORY, new ArrayList<>());
                optionsParameters.get(Specifiers.CATEGORY).add(category);
            }

            if (Validations.correctString(country)) {

                optionsParameters.put(Specifiers.COUNTRY, new ArrayList<>());
                optionsParameters.get(Specifiers.COUNTRY).add(country);
            }

            fillEntryQuery();
        }

        public URI newBuilder() throws URISyntaxException {
            return new URI(apiEndPointScheme, apiEndPointHost, apiEndPointPath, apiEndPointQuery, null);
        }

    }
}
