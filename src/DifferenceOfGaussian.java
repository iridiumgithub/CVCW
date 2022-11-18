public class DifferenceOfGaussian {
    ImagePGM imagePGM = new ImagePGM();

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
        ImagePGM[] gaussianP = new ImagePGM[5];
        for (int i = 0; i < 5; i++) {
            gaussianP[i] = new ImagePGM(imagePGM);
        }

        double d[] = {1,2,4,8,16};
        for (int i = 0; i < 5; i++) {
            //img
            ImagePGM imagePGM1 = new ImagePGM(imagePGM.depth,imagePGM.width+2*(3*(int)d[i]),imagePGM.height+2*(3*(int)d[i]));
            for (int j = 3*(int)d[i]; j < imagePGM1.width-3*(int)d[i]; j++) {
                for (int k = 3*(int)d[i]; k < imagePGM1.height-3*(int)d[i]; k++) {
                    imagePGM1.pixels[j][k] = imagePGM.pixels[j-3*(int)d[i]][k-3*(int)d[i]];
                }
            }
            //border_default
            int level = 0;
            for (int j = (int)d[i]*3-1; j > 0 ; j--) {
                imagePGM1.pixels[j][j] = imagePGM1.pixels[j+level][j+level];
                imagePGM1.pixels[imagePGM1.width-j][j] = imagePGM1.pixels[imagePGM1.width-(int)d[i]*3-1-level][(int)d[i]*3-1+level];
                imagePGM1.pixels[j][imagePGM1.height-j] = imagePGM1.pixels[(int)d[i]*3][imagePGM1.height-(int)d[i]*3-1-level];
                imagePGM1.pixels[imagePGM1.width-j][imagePGM1.height-j] = imagePGM1.pixels[imagePGM1.width-(int)d[i]*3-1-level][imagePGM1.height-(int)d[i]*3-1-level];
                for (int k = j+1; k < imagePGM1.width-j; k++) {
                    imagePGM1.pixels[k][j] = imagePGM1.pixels[k][(int)d[i]*3-1+level];
                    imagePGM1.pixels[k][imagePGM1.height-j] = imagePGM1.pixels[k][imagePGM1.height-(int)d[i]*3-1-level];
                }
                for (int k = j+1; k < imagePGM1.height-j; k++) {
                    imagePGM1.pixels[j][k] = imagePGM1.pixels[(int)d[i]*3-1+level][k];
                    imagePGM1.pixels[imagePGM1.width-j][k] = imagePGM1.pixels[imagePGM1.width-(int)d[i]*3-1-level][k];
                }
                level++;
            }
            //core
            double[][] gaussian = gaussianCore(d[i]);
            //gaussian

            for (int j = 0; j < imagePGM1.width-d[i]*6; j++) {
                for (int k = 0; k < imagePGM1.height-d[i]*6; k++) {
                    double tempg = 0;
                    if (i == 0){
                        for (int l = 0; l < d[i]*6+1; l++) {
                            for (int m = 0; m < d[i]*6+1; m++) {
                                tempg += gaussian[l][m]*imagePGM1.pixels[j+l][k+m];
                            }
                        }
                        gaussianP[i].pixels[j][k] = (int)tempg;
                    }
                }
            }
        }

        ImagePGM DoG[] = new ImagePGM[4];
        for (int i = 0; i < 4; i++) {
            DoG[i] = new ImagePGM(imagePGM.depth,imagePGM.width,imagePGM.height);
            for (int j = 0; j < imagePGM.width; j++) {
                for (int k = 0; k < imagePGM.height; k++) {
                    DoG[i].pixels[j][k] = Math.abs(gaussianP[i].pixels[j][k]-gaussianP[i+1].pixels[j][k]);

                }
            }

        }
        ImagePGM DDoG = new ImagePGM(imagePGM.depth,imagePGM.width,imagePGM.height);
        int min = Math.abs((DoG[0].pixels[0][0] + DoG[1].pixels[0][0] + DoG[2].pixels[0][0] + DoG[3].pixels[0][0])/4);
        int max = Math.abs((DoG[0].pixels[0][0] + DoG[1].pixels[0][0] + DoG[2].pixels[0][0] + DoG[3].pixels[0][0])/4);
        for (int i = 0; i < imagePGM.width; i++) {
            for (int j = 0; j < imagePGM.height; j++) {
                DDoG.pixels[i][j] = (DoG[0].pixels[i][j] + DoG[1].pixels[i][j] + DoG[2].pixels[i][j] + DoG[3].pixels[i][j])/4;
                if(DDoG.pixels[i][j] > max){
                    max = DDoG.pixels[i][j];
                }
                if(DDoG.pixels[i][j] < min){
                    min = DDoG.pixels[i][j];
                }
            }
        }
        for (int j = 0; j < imagePGM.width; j++) {
            for (int k = 0; k < imagePGM.height; k++) {
                DDoG.pixels[j][k] = (DDoG.pixels[j][k] - min) *255 / (max - min);
            }
        }
        DDoG.WritePGM("DoG.pgm");
        for (int j = 0; j < imagePGM.width; j++) {
            for (int k = 0; k < imagePGM.height; k++) {
                if (DDoG.pixels[j][k] >= 64){
                    DDoG.pixels[j][k] = 255;
                }else{
                    DDoG.pixels[j][k] = 0;
                }
            }
        }
        DDoG.WritePGM("DoGthresh.pgm");
        return null;
    }

    public int MaxPixels(int a,int b,int c){
        return Math.max(Math.max(a,b),c);
    }

    public double[][] gaussianCore(double d){
        double sum = 0;
        int size = (int)d*6+1;
        int center= 2;
        double A=1/(2*Math.PI*d*d);
        double[][] C = new double[size][size];
        for (int i = 1; i <= size; i++) {
            double x2 = (i - center) * (i - center);
            for (int j = 1; j <= size; j++) {
                double y2 = (j - center) * (j - center);
                double B = Math.pow(Math.E,-(x2+y2)/(2*d*d));
                C[i-1][j-1] = A*B;
                sum += C[i-1][j-1];
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                C[i][j] = C[i][j] / sum;
            }
        }
        return C;
    }
}
