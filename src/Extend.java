public class Extend {
    ImagePPM imagePPM;
    ImagePGM imagePGMSobel;
    ImagePGM imagePGMSobelOrientation;
    ImagePGM imagePGMDoGthresh;
    ImagePPM imagePPM0;

    public Extend(ImagePPM imagePPM0,ImagePPM imagePPM,ImagePGM imagePGMSobel,ImagePGM imagePGMSobelOrientation,ImagePGM imagePGMDoGthresh){
        this.imagePPM0 = new ImagePPM(imagePPM0);
        this.imagePPM = new ImagePPM(imagePPM);
        this.imagePGMSobel = new ImagePGM(imagePGMSobel);
        this.imagePGMSobelOrientation = new ImagePGM(imagePGMSobelOrientation);
        this.imagePGMDoGthresh = new ImagePGM(imagePGMDoGthresh);
    }

    public void function(ImagePGM brush1,ImagePGM brush2){
        ImagePPM imagePPMExtend = new ImagePPM(imagePPM);
        for (int i = 0; i < imagePPM.width; i++) {
            for (int j = 0; j < imagePPM.height; j++) {
                if(imagePPMExtend.pixels[i][j][0] == 255 && imagePPMExtend.pixels[i][j][1] == 255 && imagePPMExtend.pixels[i][j][2] == 255){
                    //select brush
                    if (imagePGMDoGthresh.pixels[i][j] == 0){
                        //select brush0
                        //Choose orientation
                        int orientation = imagePGMSobelOrientation.pixels[i][j];
                        //Choose size
                        int size = 0;
                        for (int k = 0; k < brush1.width/2; k++) {
                            if (imagePPM.width > i+k && imagePPM.height > j+k){
                                if (imagePPMExtend.pixels[i+k][j+k][0] != 255 && imagePPMExtend.pixels[i+k][j+k][1] != 255 && imagePPMExtend.pixels[i+k][j+k][2] != 255){
                                    size  = (int)Math.ceil(k*5.0/brush1.width);
                                    break;
                                }
                            }else{
                                size  = (int)Math.ceil(k*5.0/brush1.width);
                                break;
                            }
                        }
                        //benefit
                        int avgColor[] = new int[3];
                        int red = 0,green = 0,blue = 0;
                        int red0 = 0,green0 = 0,blue0 = 0;
                        int number = 0;
                        ImagePGM brush = new ImagePGM();
                        brush.ReadPGM("brush0-"+size+"-"+orientation);
                        for (int k = 0; k < brush.width/2; k++) {
                            for (int l = 0; l < brush.height/2; l++) {
                                if (brush.pixels[j][k] == 0){
                                    if (imagePPMExtend.width > i+k && imagePPMExtend.height > j+k){
                                        red += imagePPM.pixels[i+k][j+l][0];
                                        green += imagePPM.pixels[i+k][j+l][1];
                                        blue += imagePPM.pixels[i+k][j+l][2];
                                        red0 += imagePPM0.pixels[i+k][j+l][0];
                                        green0 += imagePPM0.pixels[i+k][j+l][1];
                                        blue0 += imagePPM0.pixels[i+k][j+l][2];
                                        number++;
                                    }
                                }
                            }
                        }
                        if (number == 0) {
                            break;
                        }
                        avgColor[0] = (int)(red / number);
                        avgColor[1] = (int)(green / number);
                        avgColor[2] = (int)(blue / number);
                        //measure dissimilarity
                        int dissimilarity[] = {0,0,0};
                        int dissimilarity0[] = {0,0,0};
                        for (int k = 0; k < brush.width/2; k++) {
                            for (int l = 0; l < brush.height/2; l++) {
                                if (brush.pixels[k][l] == 0){
                                    if (imagePPM.width > i+k && imagePPM.height > j+l) {
                                        dissimilarity[0] += Math.abs(avgColor[0] - imagePPM0.pixels[i+k][j+l][0]);
                                        dissimilarity[1] += Math.abs(avgColor[1] - imagePPM0.pixels[i+k][j+l][1]);
                                        dissimilarity[2] += Math.abs(avgColor[2] - imagePPM0.pixels[i+k][j+l][2]);
                                        dissimilarity0[0] += Math.abs(red0/number - imagePPM0.pixels[i+k][j+l][0]);
                                        dissimilarity0[1] += Math.abs(green0/number - imagePPM0.pixels[i+k][j+l][1]);
                                        dissimilarity0[2] += Math.abs(blue0/number - imagePPM0.pixels[i+k][j+l][2]);
                                    }
                                }
                            }
                        }
                        if (dissimilarity0[0] + dissimilarity0[1] + dissimilarity0[2] > dissimilarity[0] + dissimilarity[1] + dissimilarity[2]){
                            //paint
                            for (int k = 0; k < brush.width/2; k++){
                                for (int l = 0; l < brush.height/2; l++) {
                                    if(brush.pixels[j][k] == 0){
                                        if (imagePPM.width > i+k && imagePPM.height > j+l){
                                            if (imagePPMExtend.pixels[i+k][j+l][0] == 255 && imagePPMExtend.pixels[i+k][j+l][1] == 255 && imagePPMExtend.pixels[i+k][j+l][2] == 255){
                                                imagePPMExtend.pixels[i+k][j+l][0] = avgColor[0];
                                                imagePPMExtend.pixels[i+k][j+l][1] = avgColor[1];
                                                imagePPMExtend.pixels[i+k][j+l][2] = avgColor[2];
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }else{
                    if (imagePGMDoGthresh.pixels[i][j] == 0){
                        //select brush0
                        //Choose orientation
                        int orientation = imagePGMSobelOrientation.pixels[i][j];
                        //Choose size
                        int size = 0;
                        for (int k = 0; k < brush2.width/2; k++) {
                            if (imagePPM.width > i+k && imagePPM.height > j+k){
                                if (imagePPMExtend.pixels[i+k][j+k][0] != 255 && imagePPMExtend.pixels[i+k][j+k][1] != 255 && imagePPMExtend.pixels[i+k][j+k][2] != 255){
                                    size  = (int)Math.ceil(k*5.0/brush2.width);
                                    break;
                                }
                            }else{
                                size  = (int)Math.ceil(k*5.0/brush2.width);
                                break;
                            }
                        }
                        //benefit
                        int avgColor[] = new int[3];
                        int red = 0,green = 0,blue = 0;
                        int red0 = 0,green0 = 0,blue0 = 0;
                        int number = 0;
                        ImagePGM brush = new ImagePGM();
                        brush.ReadPGM("brush-"+size+"-"+orientation);
                        for (int k = 0; k < brush.width/2; k++) {
                            for (int l = 0; l < brush.height/2; l++) {
                                if (brush.pixels[j][k] == 0){
                                    if (imagePPMExtend.width > i+k && imagePPMExtend.height > j+k){
                                        red += imagePPM.pixels[i+k][j+l][0];
                                        green += imagePPM.pixels[i+k][j+l][1];
                                        blue += imagePPM.pixels[i+k][j+l][2];
                                        red0 += imagePPM0.pixels[i+k][j+l][0];
                                        green0 += imagePPM0.pixels[i+k][j+l][1];
                                        blue0 += imagePPM0.pixels[i+k][j+l][2];
                                        number++;
                                    }
                                }
                            }
                        }
                        if (number == 0) {
                            break;
                        }
                        avgColor[0] = (int)(red / number);
                        avgColor[1] = (int)(green / number);
                        avgColor[2] = (int)(blue / number);
                        //measure dissimilarity
                        int dissimilarity[] = {0,0,0};
                        int dissimilarity0[] = {0,0,0};
                        for (int k = 0; k < brush.width/2; k++) {
                            for (int l = 0; l < brush.height/2; l++) {
                                if (brush.pixels[k][l] == 0){
                                    if (imagePPMExtend.width > i+k && imagePPMExtend.height > j+l) {
                                        dissimilarity[0] += Math.abs(avgColor[0] - imagePPM0.pixels[i+k][j+l][0]);
                                        dissimilarity[1] += Math.abs(avgColor[1] - imagePPM0.pixels[i+k][j+l][1]);
                                        dissimilarity[2] += Math.abs(avgColor[2] - imagePPM0.pixels[i+k][j+l][2]);
                                        dissimilarity0[0] += Math.abs(red0/number - imagePPM0.pixels[i+k][j+l][0]);
                                        dissimilarity0[1] += Math.abs(green0/number - imagePPM0.pixels[i+k][j+l][1]);
                                        dissimilarity0[2] += Math.abs(blue0/number - imagePPM0.pixels[i+k][j+l][2]);
                                    }
                                }
                            }
                        }
                        if (dissimilarity0[0] + dissimilarity0[1] + dissimilarity0[2] > dissimilarity[0] + dissimilarity[1] + dissimilarity[2]){
                            //paint
                            for (int k = 0; k < brush.width/2; k++){
                                for (int l = 0; l < brush.height/2; l++) {
                                    if(brush.pixels[j][k] == 0){
                                        if (imagePPM.width > i+k && imagePPM.height > j+l){
                                            if (imagePPMExtend.pixels[i+k][j+l][0] == 255 && imagePPMExtend.pixels[i+k][j+l][1] == 255 && imagePPMExtend.pixels[i+k][j+l][2] == 255){
                                                imagePPMExtend.pixels[i+k][j+l][0] = avgColor[0];
                                                imagePPMExtend.pixels[i+k][j+l][1] = avgColor[1];
                                                imagePPMExtend.pixels[i+k][j+l][2] = avgColor[2];
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
        //smooth
        int[][] core = {
                {1,2,1},
                {2,4,2},
                {1,2,1}
        };

        ImagePPM imagePPME = new ImagePPM(imagePPMExtend.depth,imagePPMExtend.width,imagePPM.height);
        for (int i = 0; i < imagePPMExtend.width-2; i++) {
            for (int j = 0; j < imagePPMExtend.height-2; j++) {
                int tempr = 0;
                int tempg = 0;
                int tempb = 0;
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        tempr += core[k][l] * imagePPMExtend.pixels[i+k][j+l][0];
                        tempg += core[k][l] * imagePPMExtend.pixels[i+k][j+l][1];
                        tempb += core[k][l] * imagePPMExtend.pixels[i+k][j+l][2];
                    }
                }
                imagePPME.pixels[i+1][j+1][0] = tempr/16;
                imagePPME.pixels[i+1][j+1][1] = tempg/16;
                imagePPME.pixels[i+1][j+1][2] = tempb/16;
            }
        }

        for (int i = 0; i < 3; i++) {
            imagePPME.pixels[0][0][i] = imagePPME.pixels[1][1][i];
            imagePPME.pixels[imagePPME.width-1][0][i] = imagePPME.pixels[imagePPME.width-2][1][i];
            imagePPME.pixels[0][imagePPME.height-1][i] = imagePPME.pixels[1][imagePPME.height-2][i];
            imagePPME.pixels[imagePPME.width-1][imagePPME.height-1][i] = imagePPME.pixels[imagePPME.width-2][imagePPME.height-2][i];
        }
        for (int i = 1; i < imagePPME.width-1; i++) {
            for (int j = 0; j < 3; j++) {
                imagePPME.pixels[i][0][j] = imagePPME.pixels[i][1][j];
                imagePPME.pixels[i][imagePPME.height-1][j] = imagePPME.pixels[i][imagePPME.height-2][j];
            }
        }
        for (int i = 1; i < imagePPME.height-1; i++) {
            for (int j = 0; j < 3; j++) {
                imagePPME.pixels[0][i][j] = imagePPME.pixels[1][i][j];
                imagePPME.pixels[imagePPME.width-1][i][j] = imagePPME.pixels[imagePPME.width-2][i][j];
            }
        }
        imagePPME.WritePPM("resultExtend.ppm");
    }
}
