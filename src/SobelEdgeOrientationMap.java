public class SobelEdgeOrientationMap {
    public ImagePPM imagePPM;

    public SobelEdgeOrientationMap(ImagePPM imagePPM){
        this.imagePPM = new ImagePPM(imagePPM);
    }

    public ImagePGM function(){
        ImagePGM imagePGM = new ImagePGM(imagePPM.depth, imagePPM.width, imagePPM.height);
        ImagePGM imagePGMBox = new ImagePGM(imagePPM.depth, imagePPM.width-4, imagePPM.height-4);
        ImagePGM imagePGMSobel = new ImagePGM(imagePPM.depth, imagePPM.width-6, imagePPM.height-6);
        // grayscale
        for (int i = 0; i < imagePGM.width; i++) {
            for (int j = 0; j < imagePGM.height; j++) {
                imagePGM.pixels[i][j] = (imagePPM.pixels[i][j][0]+imagePPM.pixels[i][j][1]+imagePPM.pixels[i][j][2])/3;
            }
        }

        //a box filter
        double[][] boxFilter = {
                {0.04,0.04,0.04,0.04,0.04},
                {0.04,0.04,0.04,0.04,0.04},
                {0.04,0.04,0.04,0.04,0.04},
                {0.04,0.04,0.04,0.04,0.04},
                {0.04,0.04,0.04,0.04,0.04}
        };


        for (int i = 2; i < imagePGM.width-2; i++) {
            for (int j = 2; j < imagePGM.height-2; j++) {
                int tempBox = 0;

                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        tempBox += (int)(imagePGM.pixels[i-2+k][j-2+l] * boxFilter[k][l]);
                    }
                }

                imagePGMBox.pixels[i-2][j-2] = tempBox;
            }
        }
//        imagePGMBox.WritePGM("test.pgm");
        //sobel
        double[][] sobelX = {
                {1,0,-1},
                {2,0,-2},
                {1,0,-1}
        };

        double[][] sobelY = {
                {1,2,1},
                {0,0,0},
                {-1,-2,-1}
        };

        for (int i = 1; i < imagePGMBox.width-1; i++) {
            for (int j = 1; j < imagePGMBox.height-1; j++) {
                int tempX = 0;
                int tempY = 0;
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        tempX += imagePGMBox.pixels[i-1+k][j-1+l]*sobelX[k][l];
                        tempY += imagePGMBox.pixels[i-1+k][j-1+l]*sobelY[k][l];
                    }
                }

//                int result = (int)Math.sqrt(tempX*tempX+tempY*tempY);
                if(tempX == 0 && tempY>0){
                    imagePGMSobel.pixels[i-1][j-1] = (int)Math.toDegrees(Math.PI/2);
                }else if(tempX == 0 && tempY<0){
                    imagePGMSobel.pixels[i-1][j-1] = (int)Math.toDegrees(-Math.PI/2);
                }else if(tempX == 0 && tempY==0){
                    imagePGMSobel.pixels[i-1][j-1] = 0;
                }else{
                    int result = (int)Math.toDegrees(Math.atan(tempY/tempX));
                    imagePGMSobel.pixels[i-1][j-1] = result;
                }



            }
        }

        // rescale to [0,255]
        int min = imagePGMSobel.pixels[1][1];
        int max = imagePGMSobel.pixels[1][1];
        for (int y = 0; y < imagePGMSobel.height; y++){
            for (int x = 0; x < imagePGMSobel.width; x++) {
                min = Math.min(min,imagePGMSobel.pixels[x][y]);
                max = Math.max(max,imagePGMSobel.pixels[x][y]);
            }
        }

        for (int y = 0; y < imagePGMSobel.height; y++) {
            for (int x = 0; x < imagePGMSobel.width; x++) {
                imagePGMSobel.pixels[x][y] = (int) ((imagePGMSobel.pixels[x][y] - min) * 255 / (max - min));
            }
        }
        return imagePGMSobel;
    }
}
