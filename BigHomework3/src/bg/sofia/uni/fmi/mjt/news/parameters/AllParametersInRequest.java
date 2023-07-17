package bg.sofia.uni.fmi.mjt.news.parameters;

import bg.sofia.uni.fmi.mjt.news.exceptions.ParameterInvalidException;
import bg.sofia.uni.fmi.mjt.news.util.RequestOptionsValidations;

import java.util.HashMap;
import java.util.Map;

public class AllParametersInRequest extends RequestOptionsValidations implements ParametersBuilderInRequest {

    public static ParametersBuilderInRequest builder() {
        return new AllParametersInRequest();
    }

    private final Map<String, String> parameters = new HashMap<>();
    @Override
    public ParametersBuilderInRequest setSearchQuery(String q) {
        parameters.put("q", q);
        return this;
    }

    @Override
    public ParametersBuilderInRequest setSearchQueryInTitle(String qInTitle) {
        parameters.put("qInTitle", qInTitle);
        return this;
    }

    @Override
    public ParametersBuilderInRequest setSources(String sources) {
        parameters.put("sources", sources);
        return this;
    }

    @Override
    public ParametersBuilderInRequest setDomains(String domains) {
        parameters.put("domains", domains);
        return this;
    }

    @Override
    public ParametersBuilderInRequest setExcludeDomains(String excludeDomains) {
        parameters.put("excludeDomains", excludeDomains);
        return this;
    }

    @Override
    public ParametersBuilderInRequest setFrom(String from) {

        parameters.put("from", from);
        return this;
    }

    @Override
    public ParametersBuilderInRequest setTo(String to) {

        parameters.put("to", to);
        return this;
    }

    @Override
    public ParametersBuilderInRequest setLanguage(String language) throws ParameterInvalidException {
        if (checkLanguageInvalid(language)) {
            throw new ParameterInvalidException("Error ! Invalid parameter : (language)" );
        }
        parameters.put("language", language);
        return this;
    }

    @Override
    public ParametersBuilderInRequest setRelevance(String relevance) throws ParameterInvalidException {
        if (checkRelevanceInvalid(relevance)) {
            throw new ParameterInvalidException("Error ! Invalid parameter : (relevance)");
        }
        parameters.put("relevance", relevance);
        return this;
    }

    @Override
    public ParametersBuilderInRequest setPageSize(int pageSize) throws ParameterInvalidException {
        if (checkPageSizeInvalid(pageSize)) {
            throw new ParameterInvalidException("Error ! Invalid parameter : (pageSize)");
        }
        parameters.put("pageSize", Integer.toString(pageSize));
        return this;
    }

    @Override
    public ParametersBuilderInRequest setPage(int page) {
        parameters.put("page", Integer.toString(page));
        return this;
    }

    @Override
    public ParametersBuilderInRequest setApiKey(String apiKey) {
        parameters.put("apiKey", apiKey);
        return this;
    }

    @Override
    public Map<String, String> build() {
        return parameters;
    }
}
