package bg.sofia.uni.fmi.mjt.sentiment;

public enum SentimentTypes {
    NEGATIVE("negative", 0.0, 1.0) ,
    SOMEWHAT_NEGATIVE("somewhat negative",  1.0, 1.5) ,
    NEUTRAL("neutral", 1.5 , 2.5),
    SOMEWHAT_POSITIVE("somewhat positive", 2.5 , 3.5) ,
    POSITIVE("positive", 3.5, 4.0);

    private final String type;
    private final double leftRange;
    private final double rightRange;
    SentimentTypes(String type, double leftRange, double rightRange) {
        this.type = type;
        this.leftRange = leftRange;
        this.rightRange = rightRange;

    }

    public String getType() {
        return type;
    }

    public double getLeftRange() {
        return leftRange;
    }

    public double getRightRange() {
        return rightRange;
    }

}
