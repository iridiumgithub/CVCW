public class Position {
    int[][] positionxy;

    public Position(){

    }

    public Position(int[][] a){
        positionxy = new int[a.length][2];
        for (int i = 0; i < a.length; i++) {
            positionxy[i][0] = a[i][0];
            positionxy[i][1] = a[i][1];
        }
    }
}
