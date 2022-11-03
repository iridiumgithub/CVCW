import java.io.*;
import java.util.*;

public class Relaxation {
    public static void main(String[] args) throws Exception {
        String fileNameIn =  args[0];
        String fileFormat = fileNameIn.substring(fileNameIn.length()-3);
        String fileNameOut = args[1];
        if (fileFormat == "PGM"){
            ImagePGM imagePGM = new ImagePGM();
            imagePGM.ReadPGM(fileNameIn);
            //
            imagePGM.WritePGM(fileNameOut);
        }else if(fileFormat == "PPM"){
            ImagePPM imagePPM = new ImagePPM();
            imagePPM.ReadPPM(fileNameIn);
            //
        }else{
            throw new Exception("ERROR:image is not correct PPM/PGM format");
        }
    }
}