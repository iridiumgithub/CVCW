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

        double d[] = {1,2,4,8};
        for (int i = 0; i < 4; i++) {
            double[][] gaussian = gaussianCore(d[i]);
            //gaussian

            for (int j = 0; j < imagePGM.width-2; j++) {
                for (int k = 0; k < imagePGM.height-2; k++) {
                    int tempg = 0;
                    for (int l = 0; l < 3; l++) {
                        for (int m = 0; m < 3; m++) {
                            tempg += gaussian[l][m]*gaussianP[0].pixels[j+l][k+m];
                        }
                    }
                    gaussianP[i+1].pixels[j+1][k+1] = tempg;
                }
            }
            gaussianP[i+1].pixels[0][0] = gaussianP[i+1].pixels[1][1];
            gaussianP[i+1].pixels[imagePGM.width-1][0] = gaussianP[i+1].pixels[imagePGM.width-2][1];
            gaussianP[i+1].pixels[0][imagePGM.height-1] = gaussianP[i+1].pixels[1][imagePGM.height-2];
            gaussianP[i+1].pixels[imagePGM.width-1][imagePGM.height-1] = gaussianP[i+1].pixels[imagePGM.width-2][imagePGM.height-2];
            for (int j = 1; j < imagePGM.width-1; j++) {
                gaussianP[i+1].pixels[j][0] = gaussianP[i+1].pixels[j][1];
                gaussianP[i+1].pixels[j][imagePGM.height-1] = gaussianP[i+1].pixels[j][imagePGM.height-2];
            }
            for (int j = 0; j < imagePGM.height; j++) {
                gaussianP[i+1].pixels[0][j] = gaussianP[i+1].pixels[1][j];
                gaussianP[i+1].pixels[imagePGM.width-1][j] = gaussianP[i+1].pixels[imagePGM.width-1][j];
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
                if (DDoG.pixels[j][k] > 64){
                    DDoG.pixels[j][k] = 254;
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
        int size = 3;
        int center= 2;
        double A=1/(2*Math.PI*d*d);
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
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                C[i][j] = C[i][j] / sum;
            }
        }
        return C;
    }
}
