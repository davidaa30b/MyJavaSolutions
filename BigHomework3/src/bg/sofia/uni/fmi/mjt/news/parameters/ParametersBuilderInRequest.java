package bg.sofia.uni.fmi.mjt.news.parameters;

import bg.sofia.uni.fmi.mjt.news.exceptions.ParameterInvalidException;

import java.util.Map;

public interface ParametersBuilderInRequest {
    ParametersBuilderInRequest setSearchQuery(String q);

    ParametersBuilderInRequest setSearchQueryInTitle(String qInTitle);

    ParametersBuilderInRequest setSources(String sources);

    ParametersBuilderInRequest setDomains(String domains);

    ParametersBuilderInRequest setExcludeDomains(String excludeDomains);

    ParametersBuilderInRequest setFrom(String from);

    ParametersBuilderInRequest setTo(String to);

    ParametersBuilderInRequest setLanguage(String language) throws ParameterInvalidException;

    ParametersBuilderInRequest setRelevance(String sortBy) throws ParameterInvalidException;

    ParametersBuilderInRequest setPageSize(int pageSize) throws ParameterInvalidException;

    ParametersBuilderInRequest setPage(int page);

    ParametersBuilderInRequest setApiKey(String apiKey);

    Map<String, String> build();
}
