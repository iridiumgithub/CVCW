public class CreateBrush {
    public ImagePGM imagePGM;

    public CreateBrush(ImagePGM imagePGM){
        this.imagePGM = imagePGM;
    }

    public void createBrushs(){

        //size
        for (int i = 5; i > 0; i--) {
            //dir
            for (int j = 0; j < 16; j++) {
                ImagePGM imagePGM1 = new ImagePGM(imagePGM.depth,imagePGM.width*(1-1/5*(5-i)), imagePGM.height*(1-1/5*(5-i)));
                int w = imagePGM.width / 5 * i;
                int h = imagePGM.height / 5 * i;
                double radians = Math.toRadians(22.5*j);
                double[][] rotationMatrix = {
                        {Math.cos(radians),Math.sin(radians),0},
                        {-Math.sin(radians),Math.cos(radians),0},
                        {-0.5*w*Math.cos(radians)+0.5*h*Math.sin(radians)+0.5*w,-0.5*w*Math.sin(radians)-0.5*h*Math.cos(radians)+0.5*h,1}
                };
                for (int k = 0; k < w; k++) {
                    for (int l = 0; l < h; l++) {

                    }
                }

            }
        }
    }
}
