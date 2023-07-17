public class TourGuide {
    public static int getBestSightseeingPairScore(int[] places){
        int max=0;
        for(int i=0;i<places.length;i++){
            for(int j=0;j<places.length;j++){
                if(i<j){
                    int sum=places[i] + places[j] + i - j;
                    if(max<sum)
                        max=sum;
                }
            }
        }
        return max;
    }

}
