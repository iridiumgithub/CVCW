public class Relaxation {
    public static void main(String[] args) throws java.io.IOException {
        String fileNameIn =  args[0];
//        String fileFormat = fileNameIn.substring(fileNameIn.length()-3);
        String fileNameOut = args[1];
        Image image = new Image();
        image.ReadPGM(fileNameIn);
        image.WritePGM(fileNameOut);


    }
}