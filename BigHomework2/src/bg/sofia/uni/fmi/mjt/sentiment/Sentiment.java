package bg.sofia.uni.fmi.mjt.sentiment;

public class Sentiment implements Comparable<Sentiment> {
    private boolean flag;
    private double totalSentiment;
    private double timesFound;

    public Sentiment(double totalSentiment, double timesFound) {
        this.totalSentiment = totalSentiment;
        this.timesFound = timesFound;
        this.flag = true;
    }

    public double getAverage() {
        return totalSentiment / timesFound;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return flag;
    }

    public void increaseSentiment(double score) {
        this.totalSentiment += score;
    }

    public void increaseTimeFound(double found) {
        this.timesFound += found;
    }

    public void showCurrInfo() {
        System.out.println("Total sentiment : " + totalSentiment + ", times found : " + timesFound);
    }

    public double getTotalSentiment() {
        return totalSentiment;
    }

    public double getTimesFound() {
        return timesFound;
    }


    @Override
    public int compareTo(Sentiment other) {
        return Double.compare(this.getAverage(), other.getAverage());
    }
}
