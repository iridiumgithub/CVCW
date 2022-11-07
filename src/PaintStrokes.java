public class PaintStrokes {
    ImagePPM imagePGMBackground;
    ImagePPM imagePPM;
    ImagePGM imagePGM;
    ImagePGM imagePGMSobel;

    public PaintStrokes(ImagePPM imagePPM,ImagePGM imagePGMSobel){
        this.imagePPM.depth = imagePPM.depth;
        this.imagePGMSobel.depth = imagePGMSobel.depth;
        this.imagePPM.width = imagePPM.width;
        this.imagePGMSobel.width = imagePGMSobel.width;
        this.imagePPM.height = imagePPM.height;
        this.imagePGMSobel.height = imagePGMSobel.height;
        for (int i = 0; i < imagePPM.width; i++) {
            for (int j = 0; j < imagePPM.height; j++) {
                this.imagePPM.pixels[i][j][0] = imagePPM.pixels[i][j][0];
                this.imagePPM.pixels[i][j][1] = imagePPM.pixels[i][j][1];
                this.imagePPM.pixels[i][j][2] = imagePPM.pixels[i][j][2];
                this.imagePGMBackground.pixels[i][j][0] = 255;
                this.imagePGMBackground.pixels[i][j][1] = 255;
                this.imagePGMBackground.pixels[i][j][2] = 255;
                this.imagePGMSobel.pixels[i][j] = imagePGMSobel.pixels[i][j];
            }
        }
    }

    public void PaintStrokes(double density){
        ImagePGM imagePGMSobelOrientation = new ImagePGM(imagePPM.depth,imagePPM.width,imagePPM.height);
        // grayscale
        for (int i = 0; i < imagePPM.width; i++) {
            for (int j = 0; j < imagePPM.height; j++) {
                imagePGM.pixels[i][j] = imagePPM.pixels[i][j][0]+imagePPM.pixels[i][j][1]+imagePPM.pixels[i][j][2];
            }
        }
        //sobel
        double[][] sobelX = {
                {1,0,-1},
                {2,0,2},
                {1,0,-1}
        };

        double[][] sobelY = {
                {1,2,1},
                {0,0,0},
                {-1,-2,-1}
        };

        for (int i = 1; i < imagePPM.width-1; i++) {
            for (int j = 1; j < imagePPM.height-1; j++) {
                int tempX = 0;
                int tempY = 0;
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        tempX += imagePGM.pixels[i-1+k][i-1+l]*sobelX[k][l];
                        tempY += imagePGM.pixels[i-1+k][i-1+l]*sobelY[k][l];
                    }
                }

                int orientationResult = (int) Math.round(Math.toDegrees(Math.atan(tempY/tempX))/22.5);
                imagePGMSobelOrientation.pixels[i-1][j-1] = orientationResult;
            }
        }

        for (int size = 5; size > 0; size--) {
            //number of the brush
            int N = (int)(imagePGM.width*imagePGM.height*density*Math.pow(2,5-size));
            //randomly
            int position[][] = new int[N][2];
            for (int i = 0; i < N; i++) {
                position[i][0] = (int)(Math.random()*(imagePGMSobel.width));
                position[i][1] = (int)(Math.random()*(imagePGMSobel.height));
            }
            //brush
            for (int i = 0; i < N; i++) {
                //jedge
                if (imagePGMSobel.pixels[position[i][0]][position[i][1]] >= (5-size)*51 && imagePGMSobel.pixels[position[i][0]][position[i][1]] < (6-size) * 51) {
                    //select
                    ImagePGM imagePGMBrush = new ImagePGM();
                    String brushName = "brush-"+size+"-"+imagePGMSobelOrientation.pixels[position[i][0]][position[i][1]];
                    imagePGMBrush.ReadPGM(brushName);
                    int avgColor[] = new int[3];
                    int red = 0,green = 0,blue = 0;
                    int number = 0;
                    for (int j = 0; j < imagePGMBrush.width; j++) {
                        for (int k = 0; k < imagePGMBrush.height; k++) {
                            if(imagePGMBrush.pixels[i][j] == 0){
                                red += imagePPM.pixels[i-imagePGMBrush.width/2 + position[i][0]][j-imagePGMBrush.height/2 + position[i][1]][0];
                                green += imagePPM.pixels[i-imagePGMBrush.width/2 + position[i][0]][j-imagePGMBrush.height/2 + position[i][1]][1];
                                blue += imagePPM.pixels[i-imagePGMBrush.width/2 + position[i][0]][j-imagePGMBrush.height/2 + position[i][1]][2];
                                number++;
                            }
                        }
                    }
                    avgColor[0] = (int)(red / number);
                    avgColor[1] = (int)(green / number);
                    avgColor[2] = (int)(blue / number);
                    //measure dissimilarity
                    int dissimilarity[] = {0,0,0};
                    for (int j = 0; j < imagePGMBrush.width; j++) {
                        for (int k = 0; k < imagePGMBrush.height; k++) {
                            if(imagePGMBrush.pixels[i][j] == 0){
                                dissimilarity[0] += Math.abs(avgColor[0] - imagePPM.pixels[i-imagePGMBrush.width/2 + position[i][0]][j-imagePGMBrush.height/2 + position[i][1]][0]);
                                dissimilarity[1] += Math.abs(avgColor[1] - imagePPM.pixels[i-imagePGMBrush.width/2 + position[i][0]][j-imagePGMBrush.height/2 + position[i][1]][1]);
                                dissimilarity[2] += Math.abs(avgColor[2] - imagePPM.pixels[i-imagePGMBrush.width/2 + position[i][0]][j-imagePGMBrush.height/2 + position[i][1]][2]);
                            }
                        }
                    }
                    //benefit
                    if ()
                    //paint
                    for (int j = 0; j < imagePGMBrush.width; j++) {
                        for (int k = 0; k < imagePGMBrush.height; k++) {
                            if(imagePGMBrush.pixels[i][j] == 0){
                                imagePGMBackground.pixels[i-imagePGMBrush.width/2 + position[i][0]][j-imagePGMBrush.height/2 + position[i][1]][0] = avgColor[0];
                                imagePGMBackground.pixels[i-imagePGMBrush.width/2 + position[i][0]][j-imagePGMBrush.height/2 + position[i][1]][1] = avgColor[1];
                                imagePGMBackground.pixels[i-imagePGMBrush.width/2 + position[i][0]][j-imagePGMBrush.height/2 + position[i][1]][2] = avgColor[2];
                            }
                        }
                    }
                }
            }
        }
    }
}
