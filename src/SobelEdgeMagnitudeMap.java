import java.io.*;
import java.util.*;

public class SobelEdgeMagnitudeMap {
    public ImagePPM imagePPM;

    public SobelEdgeMagnitudeMap(ImagePPM imagePPM){
        this.imagePPM = new ImagePPM(imagePPM);
    }

    public ImagePGM function(){
        ImagePGM[] RedPyramid = new ImagePGM[7];
        ImagePGM[] GreenPyramid = new ImagePGM[7];
        ImagePGM[] BluePyramid = new ImagePGM[7];
        ImagePGM[] pyramid = new ImagePGM[7];
        ImagePGM[] finalPyramid = new ImagePGM[7];
        //init
//        for (int i = 0; i < 7; i++) {
            RedPyramid[0] = new ImagePGM();
            GreenPyramid[0] = new ImagePGM();
            BluePyramid[0] = new ImagePGM();
            pyramid[0] = new ImagePGM();
//        }
        int levelNumber = 1;


        //level 0
        for (int j = 0; j < imagePPM.width; j++) {
            for (int k = 0; k < imagePPM.height; k++) {
                RedPyramid[0].pixels[j][k] = imagePPM.pixels[j][k][0];
                RedPyramid[0].width = imagePPM.width;
                RedPyramid[0].height = imagePPM.height;
                RedPyramid[0].depth = imagePPM.depth;

                GreenPyramid[0].pixels[j][k] = imagePPM.pixels[j][k][1];
                GreenPyramid[0].width = imagePPM.width;
                GreenPyramid[0].height = imagePPM.height;
                GreenPyramid[0].depth = imagePPM.depth;

                BluePyramid[0].pixels[j][k] = imagePPM.pixels[j][k][2];
                BluePyramid[0].width = imagePPM.width;
                BluePyramid[0].height = imagePPM.height;
                BluePyramid[0].depth = imagePPM.depth;
            }
        }

        //level 1-END
        for(; RedPyramid[levelNumber-1].width/2 >= 20 && RedPyramid[levelNumber-1].height/2 >= 20;levelNumber++){
            RedPyramid[levelNumber] = new ImagePGM(createPyramid(RedPyramid[levelNumber-1]));
            GreenPyramid[levelNumber] = new ImagePGM(createPyramid(GreenPyramid[levelNumber-1]));
            BluePyramid[levelNumber] = new ImagePGM(createPyramid(BluePyramid[levelNumber-1]));
//            RedPyramid[levelNumber].CopyPGM(createPyramid(RedPyramid[levelNumber-1]));
//            GreenPyramid[levelNumber].CopyPGM(createPyramid(GreenPyramid[levelNumber-1]));
//            BluePyramid[levelNumber].CopyPGM(createPyramid(BluePyramid[levelNumber-1]));
//            RedPyramid[levelNumber].width = createPyramid(RedPyramid[levelNumber-1]).width;
//            GreenPyramid[levelNumber].width = createPyramid(GreenPyramid[levelNumber-1]).width;
//            BluePyramid[levelNumber].width = createPyramid(BluePyramid[levelNumber-1]).width;
//            RedPyramid[levelNumber].height = createPyramid(RedPyramid[levelNumber-1]).height;
//            GreenPyramid[levelNumber].height = createPyramid(GreenPyramid[levelNumber-1]).height;
//            BluePyramid[levelNumber].height = createPyramid(BluePyramid[levelNumber-1]).height;
//            RedPyramid[levelNumber].depth = createPyramid(RedPyramid[levelNumber-1]).depth;
//            GreenPyramid[levelNumber].depth = createPyramid(GreenPyramid[levelNumber-1]).depth;
//            BluePyramid[levelNumber].depth = createPyramid(BluePyramid[levelNumber-1]).depth;
//            for(int i = 0; i < (RedPyramid[levelNumber-1].width)/2;i++){
//                for (int j = 0; j < (RedPyramid[levelNumber-1].height); j++) {
//                    RedPyramid[levelNumber].pixels[i][j] = createPyramid(RedPyramid[levelNumber-1]).pixels[i][j];
//                    GreenPyramid[levelNumber].pixels[i][j] = createPyramid(GreenPyramid[levelNumber-1]).pixels[i][j];
//                    BluePyramid[levelNumber].pixels[i][j] = createPyramid(BluePyramid[levelNumber-1]).pixels[i][j];
//                }
//            }
        }
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

        for(int i = 0; i < levelNumber;i++){
            pyramid[i] = new ImagePGM();
            pyramid[i].height = RedPyramid[i].height;
            pyramid[i].width = RedPyramid[i].width;
            pyramid[i].depth = RedPyramid[i].depth;
            for(int j = 0; j < RedPyramid[i].width-2;j++){
                for (int k = 0; k < RedPyramid[i].height-2; k++) {
                    int tempXR = 0,tempXG = 0,tempXB = 0;
                    int tempYR = 0,tempYG = 0,tempYB = 0;

                    for (int l = 0; l < 3; l++) {
                        for (int m = 0; m < 3; m++) {
                            tempXR += RedPyramid[i].pixels[l+j][m+k]*sobelX[l][m];
                            tempYR += RedPyramid[i].pixels[l+j][m+k]*sobelY[l][m];
                            tempXG += GreenPyramid[i].pixels[l+j][m+k]*sobelX[l][m];
                            tempYG += GreenPyramid[i].pixels[l+j][m+k]*sobelY[l][m];
                            tempXB += BluePyramid[i].pixels[l+j][m+k]*sobelX[l][m];
                            tempYB += BluePyramid[i].pixels[l+j][m+k]*sobelY[l][m];
                        }
                    }
                    //max
                    int resultRed = (int) Math.sqrt(tempXR*tempXR+tempYR*tempYR);
                    int resultGreen = (int) Math.sqrt(tempXG*tempXG+tempYG*tempYG);
                    int resultBlue = (int) Math.sqrt(tempXB*tempXB+tempYB*tempYB);
                    int maxResult = MAXNUMBER(resultRed,resultGreen,resultBlue);

                    pyramid[i].pixels[j+1][k+1] =  maxResult;
                }
            }
            //filling
            pyramid[i].pixels[0][0] = pyramid[i].pixels[1][1];
            pyramid[i].pixels[0][pyramid.length-1] = pyramid[i].pixels[1][pyramid.length-2];
            pyramid[i].pixels[imagePPM.width-1][0] = pyramid[i].pixels[imagePPM.width-2][1];
            pyramid[i].pixels[imagePPM.width-1][pyramid.length-1] = pyramid[i].pixels[imagePPM.width-2][pyramid.length-2];
            for (int j = 1; j < pyramid[i].width; j++) {
                pyramid[i].pixels[0][j] = pyramid[i].pixels[1][j];
                pyramid[i].pixels[imagePPM.width-1][j] = pyramid[i].pixels[imagePPM.width-2][j];
            }
            for (int j = 01; j < pyramid[i].height; j++) {
                pyramid[i].pixels[j][0] = pyramid[i].pixels[j][1];
                pyramid[i].pixels[j][pyramid.length-1] = pyramid[i].pixels[j][pyramid.length-2];
            }
        }

        //
        for (int i = 0; i < levelNumber; i++) {
            finalPyramid[i] = new ImagePGM();
            finalPyramid[i].width = pyramid[0].width;
            finalPyramid[i].height = pyramid[0].height;
            finalPyramid[i].depth = pyramid[0].depth;
        }

        //bilinear interpolation
//        for (int i = levelNumber-1; i > 0; i--) {
//            ImagePGM imagePGMtemp1 = new ImagePGM(pyramid[i]);
//            for (int m = i; m > 0; m--) {
//                if (pyramid[m].width*2 == pyramid[m-1].width){
//                    ImagePGM imagePGMtemp = new ImagePGM(imagePGMtemp1.depth,imagePGMtemp1.width*2,imagePGMtemp1.height*2);
//                    for (int j = 0; j < imagePGMtemp.width; j++) {
//                        for (int k = 0; k < imagePGMtemp.height; k++) {
//                            if(j%2==0 && k%2==0){
//                                imagePGMtemp.pixels[j][k] = imagePGMtemp1.pixels[j/2][k/2];
//                            }else if(j%2!=0 && k%2==0){
//                                imagePGMtemp.pixels[j][k] = (imagePGMtemp1.pixels[j/2][k/2] + imagePGMtemp1.pixels[j/2 + 1][k/2])/2;
//                            }else if(j%2==0 && k%2!=0){
//                                imagePGMtemp.pixels[j][k] = (imagePGMtemp1.pixels[m/2][j/2] + imagePGMtemp1.pixels[j/2][k/2 + 1])/2;
//                            }else{
//                                imagePGMtemp.pixels[j][k] = ((imagePGMtemp1.pixels[m/2][j/2] + imagePGMtemp1.pixels[j/2][k/2 + 1])/2 +
//                                        (imagePGMtemp1.pixels[j/2 + 1][k/2] + imagePGMtemp1.pixels[j/2 + 1][k/2 + 1])/2)/2;
//                            }
//                        }
//                    }
//                    for (int j = 0; j < pyramid[m].width*2; j++) {
//                        for (int k = 0; k < pyramid[m].height*2; k++) {
////                            pyramid[m].pixels[j][k] = (pyramid[m].pixels[j][k]+imagePGMtemp.pixels[j][k])/2;
//                            imagePGMtemp1.pixels[j][k] = imagePGMtemp.pixels[j][k];
//                        }
//                    }
//                }else {
//                    ImagePGM imagePGMtemp = new ImagePGM(imagePGMtemp1.depth,imagePGMtemp1.width*2 + 1,imagePGMtemp1.height*2 +1);
//                    for (int j = 0; j < imagePGMtemp.width-1; j++) {
//                        for (int k = 0; k < imagePGMtemp.height-1; k++) {
//                            if(j%2==0 && k%2==0){
//                                imagePGMtemp.pixels[j][k] = imagePGMtemp1.pixels[j/2][k/2];
//                            }else if(j%2!=0 && k%2==0){
//                                imagePGMtemp.pixels[j][k] = (imagePGMtemp1.pixels[j/2][k/2] + imagePGMtemp1.pixels[j/2 + 1][k/2])/2;
//                            }else if(j%2==0 && k%2!=0){
//                                imagePGMtemp.pixels[j][k] = (imagePGMtemp1.pixels[j/2][k/2] + imagePGMtemp1.pixels[j/2][k/2 + 1])/2;
//                            }else{
//                                imagePGMtemp.pixels[j][k] = ((imagePGMtemp1.pixels[j/2][k/2] + imagePGMtemp1.pixels[j/2][k/2 + 1])/2 +
//                                        (imagePGMtemp1.pixels[j/2 + 1][k/2] + imagePGMtemp1.pixels[k/2 + 1][k/2 + 1])/2)/2;
//                            }
//                        }
//                    }
//                    for (int j = 0; j < imagePGMtemp.width; j++) {
//                        imagePGMtemp.pixels[j][imagePGMtemp.height-1] = imagePGMtemp.pixels[j][imagePGMtemp.height-2];
//                    }
//                    for (int j = 0; j < imagePGMtemp.height; j++) {
//                        imagePGMtemp.pixels[imagePGMtemp.width-1][j] = imagePGMtemp.pixels[imagePGMtemp.width-2][j];
//                    }
//                    for (int j = 0; j < pyramid[m].width*2; j++) {
//                        for (int k = 0; k < pyramid[m].height*2; k++) {
//                            imagePGMtemp1.pixels[j][k] = imagePGMtemp.pixels[j][k];
//                        }
//                    }
//                }
//            }
//        }
        //bilinear interpolation pass 1
        for (int i = levelNumber-1; i > 0; i--) {
            if (pyramid[i].width*2 == pyramid[i-1].width){
                ImagePGM imagePGMtemp = new ImagePGM(pyramid[i].depth,pyramid[i].width*2,pyramid[i].height*2);
                for (int j = 0; j < imagePGMtemp.width; j++) {
                    for (int k = 0; k < imagePGMtemp.height; k++) {
                        if(j%2==0 && k%2==0){
                            imagePGMtemp.pixels[j][k] = pyramid[i].pixels[j/2][k/2];
                        }else if(j%2!=0 && k%2==0){
                            imagePGMtemp.pixels[j][k] = (pyramid[i].pixels[j/2][k/2] + pyramid[i].pixels[j/2 + 1][k/2])/2;
                        }else if(j%2==0 && k%2!=0){
                            imagePGMtemp.pixels[j][k] = (pyramid[i].pixels[j/2][k/2] + pyramid[i].pixels[j/2][k/2 + 1])/2;
                        }else{
                            imagePGMtemp.pixels[j][k] = ((pyramid[i].pixels[j/2][k/2] + pyramid[i].pixels[j/2][k/2 + 1])/2 +
                                    (pyramid[i].pixels[j/2 + 1][k/2] + pyramid[i].pixels[j/2 + 1][k/2 + 1])/2)/2;
                        }
                    }
                }
                for (int j = 0; j < pyramid[i].width*2; j++) {
                    for (int k = 0; k < pyramid[i].height*2; k++) {
                        pyramid[i].pixels[j][k] = (pyramid[i].pixels[j][k]+imagePGMtemp.pixels[j][k])/2;
                    }
                }
            }else {
                ImagePGM imagePGMtemp = new ImagePGM(pyramid[i].depth,pyramid[i].width*2 + 1,pyramid[i].height*2 +1);
                for (int j = 0; j < imagePGMtemp.width-1; j++) {
                    for (int k = 0; k < imagePGMtemp.height-1; k++) {
                        if(j%2==0 && k%2==0){
                            imagePGMtemp.pixels[j][k] = pyramid[i].pixels[j/2][k/2];
                        }else if(j%2!=0 && k%2==0){
                            imagePGMtemp.pixels[j][k] = (pyramid[i].pixels[j/2][k/2] + pyramid[i].pixels[j/2 + 1][k/2])/2;
                        }else if(j%2==0 && k%2!=0){
                            imagePGMtemp.pixels[j][k] = (pyramid[i].pixels[j/2][k/2] + pyramid[i].pixels[j/2][k/2 + 1])/2;
                        }else{
                            imagePGMtemp.pixels[j][k] = ((pyramid[i].pixels[j/2][k/2] + pyramid[i].pixels[j/2][k/2 + 1])/2 +
                                    (pyramid[i].pixels[j/2 + 1][k/2] + pyramid[i].pixels[j/2 + 1][k/2 + 1])/2)/2;
                        }
                    }
                }
                for (int j = 0; j < imagePGMtemp.width; j++) {
                    imagePGMtemp.pixels[j][imagePGMtemp.height-1] = imagePGMtemp.pixels[j][imagePGMtemp.height-2];
                }
                for (int j = 0; j < imagePGMtemp.height; j++) {
                    imagePGMtemp.pixels[imagePGMtemp.width-1][j] = imagePGMtemp.pixels[imagePGMtemp.width-2][j];
                }
                for (int j = 0; j < pyramid[i].width*2; j++) {
                    for (int k = 0; k < pyramid[i].height*2; k++) {
                        pyramid[i].pixels[j][k] = (pyramid[i].pixels[j][k]+imagePGMtemp.pixels[j][k])/2;
                    }
                }
            }

        }
        return pyramid[0];
    }

    public ImagePGM createPyramid(ImagePGM imagePGM){
        ImagePGM imagePGMNew = new ImagePGM(imagePGM.depth, (int)(imagePGM.width/2), (int)(imagePGM.height/2));
        for(int i = 0; i < imagePGMNew.width;i++){
            for(int j = 0; j < imagePGMNew.height;j++){
                imagePGMNew.pixels[i][j] = (imagePGM.pixels[i*2][j*2] + imagePGM.pixels[i*2+1][j*2] + imagePGM.pixels[i*2][j*2+1] + imagePGM.pixels[i*2+1][j*2+1])/4;
            }
        }
        return imagePGMNew;

    }

    public int MAXNUMBER(int a,int b,int c){
        return Math.max(Math.max(a,b),c);
    }


}
