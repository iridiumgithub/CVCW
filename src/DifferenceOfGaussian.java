public class DifferenceOfGaussian {
    ImagePGM imagePGM;

    public DifferenceOfGaussian(ImagePPM imagePPM){
        this.imagePGM.depth = imagePPM.depth;
        this.imagePGM.width = imagePPM.width;
        this.imagePGM.height = imagePPM.height;
        for (int i = 0; i < imagePPM.width; i++) {
            for (int j = 0; j < imagePPM.height; j++) {
                this.imagePGM.pixels[i][j] = MaxPixels(imagePPM.pixels[i][j][0],imagePPM.pixels[i][j][1],imagePPM.pixels[i][j][2]);
            }
        }
    }

    public ImagePGM function(){
        double d[] = {1,2,4,8};
        for (int i = 0; i < 4; i++) {
            double[][] gaussian = gaussianCore(i);

        }
    }

    public int MaxPixels(int a,int b,int c){
        return Math.max(Math.max(a,b),c);
    }

    public double[][] gaussianCore(double d){
        double pi=3.1415926;
        double sum = 0;
        int size = 3;
        double center=(size/2)+0.5;
        double A=1/(2*pi*d*d);
        double[][] C = new double[3][3];
        for (int i = 1; i <= 3; i++) {
            double x2 = (i - center) * (i - center);
            for (int j = 1; j <= 3; j++) {
                double y2 = (j - center) * (j - center);
                double B = Math.pow(Math.E,-(x2+y2)/(2*d*d));
                C[i-1][j-1] = A*B;
                sum += C[i-1][j-1];
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                C[i][j] = C[i][j] / sum;
            }
        }
        return C;
    }
}
