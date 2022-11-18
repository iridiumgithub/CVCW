public class CreateBrush {
    public ImagePGM imagePGM;

    public CreateBrush(ImagePGM imagePGM){
        this.imagePGM = new ImagePGM(imagePGM);
    }

    public void createBrushs(){
        //size
        for (int i = 5; i > 0; i--) {
            ImagePGM imagePGMSize = new ImagePGM(imagePGM.depth,imagePGM.width-imagePGM.width/5*(5-i), imagePGM.height-imagePGM.height/5*(5-i));
            for (int j = 0; j < imagePGM.width; j++) {
                int n = j/5;
                for (int k = 0; k < imagePGM.height; k++) {
                    int m = k/5;
                    if(((j%5)<i) && ((k%5)<i)){
                        imagePGMSize.pixels[j-n*(5-i)][k-m*(5-i)] = imagePGM.pixels[j][k]*255;
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
                int w = imagePGMSizeDir.width;
                int h = imagePGMSizeDir.height;
                double radians = Math.toRadians(22.5*j);
                double[][] rotationMatrix = {
                        {Math.cos(radians),-Math.sin(radians),0},
                        {Math.sin(radians),Math.cos(radians),0},
                        {-0.5*w*Math.cos(radians)-0.5*h*Math.sin(radians)+0.5*w,0.5*w*Math.sin(radians)-0.5*h*Math.cos(radians)+0.5*h,1}
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
                        //bilinear interpolation
                        if((x0<imagePGMSizeDir.width-1 && x0>=0)&& (y0<imagePGMSizeDir.height-1 && y0>=0)){
                            int left = (int)Math.floor(x0);
                            int right = (int)Math.ceil(x0);
                            int top = (int)Math.floor(y0);
                            int bottom = (int)Math.ceil(y0);
                            double a = x0 - left;
                            double b = y0 - top;
                            imagePGMSizeDir.pixels[k][l] = (int)((1-a)*(1-b)*imagePGMSize.pixels[top][left]+a*(1-b)*imagePGMSize.pixels[top][right]
                                    +(1-a)*b*imagePGMSize.pixels[bottom][left]+a*b*imagePGMSize.pixels[bottom][right]);
                        }else{
                            imagePGMSizeDir.pixels[k][l] = 255;
                        }
                    }
                }
                imagePGMSizeDir.WritePGM("brush-"+i+"-"+j+".PGM");
            }
        }
    }
}
