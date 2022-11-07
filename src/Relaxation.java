import java.io.*;
import java.util.*;

public class Relaxation {
    public static void main(String[] args) throws IOException {
        //read data
        String PPMFileNameIn =  args[0];
        String brushFileNameOne = args[1];
        String brushFileNameTwo = args[2];
        String density = args[3];
        String PPMFileFormat = PPMFileNameIn.substring(PPMFileNameIn.length()-3);
        String brushFileOneFormat = PPMFileNameIn.substring(brushFileNameOne.length()-3);
        String brushFileTwoFormat = PPMFileNameIn.substring(brushFileNameTwo.length()-3);

        ImagePPM imagePPM = new ImagePPM();
        imagePPM.ReadPPM(PPMFileFormat);
        SobelEdgeMagnitudeMap sobelEdgeMagnitudeMap = new SobelEdgeMagnitudeMap(imagePPM);
    }
}