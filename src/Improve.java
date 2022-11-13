public class Improve {
    ImagePPM imagePPM;
    ImagePGM imagePGMSobel;

    public Improve(ImagePPM imagePPM,ImagePGM imagePGMSobel){
        this.imagePPM = new ImagePPM(imagePPM);
        this.imagePGMSobel = new ImagePGM(imagePGMSobel);
    }

    public void function(ImagePGM brush2,ImagePGM imghresh,Position[] positions){
        ImagePPM imagePGMBackground = new ImagePPM(imagePPM.depth,imagePPM.width,imagePPM.height);
        for (int i = 0; i < imagePPM.width; i++) {
            for (int j = 0; j < imagePPM.height; j++) {
                imagePGMBackground.pixels[i][j][0] = 255;
                imagePGMBackground.pixels[i][j][1] = 255;
                imagePGMBackground.pixels[i][j][2] = 255;
            }
        }
        ImagePGM imagePGM = new ImagePGM(imagePPM.depth,imagePPM.width,imagePPM.height);
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

                int orientationResult = (int) Math.round(Math.toDegrees(Math.atan2(tempX,tempY))/22.5);
                if(orientationResult < 0){
                    imagePGMSobelOrientation.pixels[i][j] = 16 + orientationResult;
                }else {
                    imagePGMSobelOrientation.pixels[i][j] = orientationResult;
                }
                imagePGMSobelOrientation.pixels[0][0] = imagePGMSobelOrientation.pixels[1][1];
                imagePGMSobelOrientation.pixels[0][imagePGMBackground.height-1] = imagePGMSobelOrientation.pixels[1][imagePGMBackground.height-2];
                imagePGMSobelOrientation.pixels[imagePGMBackground.width-1][0] = imagePGMSobelOrientation.pixels[imagePGMBackground.width-2][1];
                imagePGMSobelOrientation.pixels[imagePGMBackground.width-1][imagePGMBackground.height-1] = imagePGMSobelOrientation.pixels[imagePGMBackground.width-2][imagePGMBackground.height-2];
                for (int k = 1; k < imagePGMBackground.width-1; k++) {
                    imagePGMSobelOrientation.pixels[k][0] = imagePGMSobelOrientation.pixels[k][1];
                }
                for (int k = 1; k < imagePGMBackground.height-1; k++) {
                    imagePGMSobelOrientation.pixels[0][k] = imagePGMSobelOrientation.pixels[1][k];
                }
            }
        }

        for (int size = 5; size > 0; size--) {
            Position position = positions[size-1];
            int N = position.positionxy.length;
            for (int i = 0; i < N; i++) {
                //jedge
                if (imagePGMSobel.pixels[position.positionxy[i][0]][position.positionxy[i][1]] >= (5 - size) * 51 && imagePGMSobel.pixels[position.positionxy[i][0]][position.positionxy[i][1]] < (6 - size) * 51) {
                    if (imghresh.pixels[position.positionxy[i][0]][position.positionxy[i][1]] == 255){
                        //select
                        ImagePGM imagePGMBrush = new ImagePGM();
                        String brushName = "brush-" + size + "-" + imagePGMSobelOrientation.pixels[position.positionxy[i][0]][position.positionxy[i][1]] + ".PGM";
                        imagePGMBrush.ReadPGM(brushName);
                        int avgColor[] = new int[3];
                        int red = 0, green = 0, blue = 0;
                        int number = 0;
                        for (int j = 0; j < imagePGMBrush.width; j++) {
                            for (int k = 0; k < imagePGMBrush.height; k++) {
                                if (imagePGMBrush.pixels[j][k] == 0) {
                                    if (j - imagePGMBrush.width / 2 + position.positionxy[i][0] >= 0 && k - imagePGMBrush.height / 2 + position.positionxy[i][1] >= 0 && j - imagePGMBrush.width / 2 + position.positionxy[i][0] < imagePGMBackground.width && k - imagePGMBrush.height / 2 + position.positionxy[i][1] < imagePGMBackground.height) {
                                        red += imagePPM.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][0];
                                        green += imagePPM.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][1];
                                        blue += imagePPM.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][2];
                                        number++;
                                    }
                                }
                            }
                        }
                        if (number == 0) {
                            break;
                        }
                        avgColor[0] = (int) (red / number);
                        avgColor[1] = (int) (green / number);
                        avgColor[2] = (int) (blue / number);
                        //measure dissimilarity
                        int dissimilarity[] = {0, 0, 0};
                        for (int j = 0; j < imagePGMBrush.width; j++) {
                            for (int k = 0; k < imagePGMBrush.height; k++) {
                                if (imagePGMBrush.pixels[j][k] == 0) {
                                    if (j - imagePGMBrush.width / 2 + position.positionxy[i][0] >= 0 && k - imagePGMBrush.height / 2 + position.positionxy[i][1] >= 0 && j - imagePGMBrush.width / 2 + position.positionxy[i][0] < imagePGMBackground.width && k - imagePGMBrush.height / 2 + position.positionxy[i][1] < imagePGMBackground.height) {
                                        dissimilarity[0] += Math.abs(avgColor[0] - imagePPM.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][0]);
                                        dissimilarity[1] += Math.abs(avgColor[1] - imagePPM.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][1]);
                                        dissimilarity[2] += Math.abs(avgColor[2] - imagePPM.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][2]);
                                    }
                                }
                            }
                        }
                        //benefit

                        //paint
                        for (int j = 0; j < imagePGMBrush.width; j++) {
                            for (int k = 0; k < imagePGMBrush.height; k++) {
                                if (imagePGMBrush.pixels[j][k] == 0) {
                                    if (j - imagePGMBrush.width / 2 + position.positionxy[i][0] >= 0 && k - imagePGMBrush.height / 2 + position.positionxy[i][1] >= 0 && j - imagePGMBrush.width / 2 + position.positionxy[i][0] < imagePGMBackground.width && k - imagePGMBrush.height / 2 + position.positionxy[i][1] < imagePGMBackground.height) {
                                        imagePGMBackground.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][0] = avgColor[0];
                                        imagePGMBackground.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][1] = avgColor[1];
                                        imagePGMBackground.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][2] = avgColor[2];
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        CreateBrush createBrush1 = new CreateBrush(brush2);
        createBrush1.createBrushs();

        for (int size = 5; size > 0; size--) {
            Position position = positions[size-1];
            int N = position.positionxy.length;
            for (int i = 0; i < N; i++) {
                //jedge
                if (imagePGMSobel.pixels[position.positionxy[i][0]][position.positionxy[i][1]] >= (5 - size) * 51 && imagePGMSobel.pixels[position.positionxy[i][0]][position.positionxy[i][1]] < (6 - size) * 51) {
                    if (imghresh.pixels[position.positionxy[i][0]][position.positionxy[i][1]] == 0){
                        //select
                        ImagePGM imagePGMBrush = new ImagePGM();
                        String brushName = "brush-" + size + "-" + imagePGMSobelOrientation.pixels[position.positionxy[i][0]][position.positionxy[i][1]] + ".PGM";
                        imagePGMBrush.ReadPGM(brushName);
                        int avgColor[] = new int[3];
                        int red = 0, green = 0, blue = 0;
                        int number = 0;
                        for (int j = 0; j < imagePGMBrush.width; j++) {
                            for (int k = 0; k < imagePGMBrush.height; k++) {
                                if (imagePGMBrush.pixels[j][k] == 0) {
                                    if (j - imagePGMBrush.width / 2 + position.positionxy[i][0] >= 0 && k - imagePGMBrush.height / 2 + position.positionxy[i][1] >= 0 && j - imagePGMBrush.width / 2 + position.positionxy[i][0] < imagePGMBackground.width && k - imagePGMBrush.height / 2 + position.positionxy[i][1] < imagePGMBackground.height) {
                                        red += imagePPM.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][0];
                                        green += imagePPM.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][1];
                                        blue += imagePPM.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][2];
                                        number++;
                                    }
                                }
                            }
                        }
                        if (number == 0) {
                            break;
                        }
                        avgColor[0] = (int) (red / number);
                        avgColor[1] = (int) (green / number);
                        avgColor[2] = (int) (blue / number);
                        //measure dissimilarity
                        int dissimilarity[] = {0, 0, 0};
                        for (int j = 0; j < imagePGMBrush.width; j++) {
                            for (int k = 0; k < imagePGMBrush.height; k++) {
                                if (imagePGMBrush.pixels[j][k] == 0) {
                                    if (j - imagePGMBrush.width / 2 + position.positionxy[i][0] >= 0 && k - imagePGMBrush.height / 2 + position.positionxy[i][1] >= 0 && j - imagePGMBrush.width / 2 + position.positionxy[i][0] < imagePGMBackground.width && k - imagePGMBrush.height / 2 + position.positionxy[i][1] < imagePGMBackground.height) {
                                        dissimilarity[0] += Math.abs(avgColor[0] - imagePPM.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][0]);
                                        dissimilarity[1] += Math.abs(avgColor[1] - imagePPM.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][1]);
                                        dissimilarity[2] += Math.abs(avgColor[2] - imagePPM.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][2]);
                                    }
                                }
                            }
                        }
                        //benefit

                        //paint
                        for (int j = 0; j < imagePGMBrush.width; j++) {
                            for (int k = 0; k < imagePGMBrush.height; k++) {
                                if (imagePGMBrush.pixels[j][k] == 0) {
                                    if (j - imagePGMBrush.width / 2 + position.positionxy[i][0] >= 0 && k - imagePGMBrush.height / 2 + position.positionxy[i][1] >= 0 && j - imagePGMBrush.width / 2 + position.positionxy[i][0] < imagePGMBackground.width && k - imagePGMBrush.height / 2 + position.positionxy[i][1] < imagePGMBackground.height) {
                                        imagePGMBackground.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][0] = avgColor[0];
                                        imagePGMBackground.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][1] = avgColor[1];
                                        imagePGMBackground.pixels[j - imagePGMBrush.width / 2 + position.positionxy[i][0]][k - imagePGMBrush.height / 2 + position.positionxy[i][1]][2] = avgColor[2];
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
