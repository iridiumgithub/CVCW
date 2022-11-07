public class CreateBrush {
    public ImagePGM imagePGM;

    public CreateBrush(ImagePGM imagePGM){
        this.imagePGM = imagePGM;
    }

    public void createBrushs(){
        //size
        for (int i = 5; i > 0; i--) {
            ImagePGM imagePGMSize = new ImagePGM(imagePGM.depth,imagePGM.width-imagePGM.width/5*(5-i), imagePGM.height-imagePGM.height/5*(5-i));
            for (int j = 0; j < imagePGM.width; j++) {
                int n = j/5;
                for (int k = 0; k < imagePGM.height; k++) {
                    int m = k/5;
                    if(!(imagePGM.width%5<(5-i)||imagePGM.height<(5-i))){
                        imagePGMSize.pixels[j-n][k-m] = imagePGM.pixels[j][k];
                    }
                }
            }
            //dir
            for (int j = 0; j < 16; j++) {
                ImagePGM imagePGMSizeDir = new ImagePGM(imagePGM.depth,imagePGMSize.width, imagePGMSize.height);
                for (int k = 0; k < imagePGMSizeDir.width; k++) {
                    for (int l = 0; l < imagePGMSizeDir.height; l++) {
                        imagePGMSizeDir.pixels[k][l] = 255;
                    }
                }
                int w = imagePGMSizeDir.width / 5 * i;
                int h = imagePGMSizeDir.height / 5 * i;
                double radians = Math.toRadians(22.5*j);
                double[][] rotationMatrix = {
                        {Math.cos(radians),Math.sin(radians),0},
                        {-Math.sin(radians),Math.cos(radians),0},
                        {-0.5*w*Math.cos(radians)-0.5*h*Math.sin(radians)+0.5*w,-0.5*w*Math.sin(radians)-0.5*h*Math.cos(radians)+0.5*h,1}
                };
                for (int k = 0; k < w; k++) {
                    for (int l = 0; l < h; l++) {
                        int[] o = {k,l,1};
                        double x0 = 0;
                        double y0 = 0;
                        for (int m = 0; m < 3; m++) {
                            x0 += o[m]*rotationMatrix[m][0];
                            y0 += o[m]*rotationMatrix[m][1];
                        }
                        int x1 = (int)x0;
                        int y1 = (int)y0;
                        if((x1<imagePGMSizeDir.width && x1>-1) && (y1<imagePGMSizeDir.height && y1>-1)){
                            imagePGMSizeDir.pixels[x1][y1] = imagePGMSize.pixels[k][l];
                        }
                    }
                }
                imagePGMSizeDir.WritePGM("brush-"+i+"-"+j);
            }
        }
    }
}
