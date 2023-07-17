package bg.sofia.uni.fmi.mjt.news.specifiers;

public enum Specifiers {
    COUNTRY("country"),
    KEYWORD("keyword"),
    CATEGORY("category"),
    PAGE("page"),
    PAGE_SIZE("pageSize"),
    API_KEY("apiKey");

    private final String parameter;

    Specifiers(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }



}
