package bg.sofia.uni.fmi.mjt.news.requests;

import bg.sofia.uni.fmi.mjt.news.dto.Source;

import java.util.LinkedList;
import java.util.List;

public class AllSources {
    private List<Source> sourceList;
    private String status;

    public AllSources() {
        sourceList = new LinkedList<>();
    }

    public int getSourcesCount() {
        return sourceList.size();
    }
}
