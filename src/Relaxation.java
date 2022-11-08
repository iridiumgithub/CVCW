import java.io.*;
import java.util.*;

public class Relaxation {
    public static void main(String[] args) throws IOException {
        //read data
        String PPMFileNameIn =  args[0];
        String brushFileNameOne = args[1];
        String brushFileNameTwo = args[2];
        String density = args[3];

        ImagePPM imagePPM = new ImagePPM();
        imagePPM.ReadPPM(PPMFileNameIn);
        ImagePGM imagePGM = new ImagePGM();
        imagePGM.ReadPGM(brushFileNameOne);
//        SobelEdgeMagnitudeMap sobelEdgeM
//        agnitudeMap = new SobelEdgeMagnitudeMap(imagePPM);
//        sobelEdgeMagnitudeMap.function().WritePGM("sobelMag.pgm");
    }
}