public class DataCenter {
    public static boolean filledUp(int[][]map){
        int n=map.length; // row
        int m=map[0].length; // col
        int count=0;
        for(int i=0;i<n;i++) {
            for (int j = 0; j < m; j++) {
                if(map[i][j]==1)
                    count++;
            }
        }
        return (count==m*n);

    }

   public static boolean checkHaveCon(int i,int j,int arr[][],int n,int m){
        int count=0;
        if(arr[i][j]==0)
            return false;

        for(int col=0;col<m;col++){
            if(arr[i][col]==1)
                count++;
        }
        for(int row=0;row<n;row++){
            if(arr[row][j]==1)
                count++;
        }

        return count>2;
    }
   public static int getCommunicatingServersCount(int[][] map){
        int connections=0;
        boolean key=false;
        int n=map.length; // row
        int m=map[0].length; // col
        if(filledUp(map))
        return n*m;

        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++) {
                if(checkHaveCon(i,j, map, n, m)==true)
                    connections++;
            }
        }


        return connections;
    }
}
