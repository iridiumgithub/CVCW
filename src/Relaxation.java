import java.io.*;
import java.util.*;

public class Relaxation {
    public static void main(String[] args) throws IOException {
        //read data
        String PPMFileNameIn =  args[0];
        String brushFileNameOne = args[1];
        String brushFileNameTwo = args[2];
        double density = Double.valueOf(args[3]);

        ImagePPM imagePPM = new ImagePPM();
        imagePPM.ReadPPM(PPMFileNameIn);
        ImagePGM imagePGMBrush1 = new ImagePGM();
        imagePGMBrush1.ReadPGM(brushFileNameOne);
//        SobelEdgeMagnitudeMap sobelEdgeMagnitudeMap = new SobelEdgeMagnitudeMap(imagePPM);
//        sobelEdgeMagnitudeMap.function().WritePGM("sobelMag.pgm");
//        SobelEdgeOrientationMap sobelEdgeOrientationMap = new SobelEdgeOrientationMap(imagePPM);
//        sobelEdgeOrientationMap.function().WritePGM("sobelDir.pgm");
//        CreateBrush createBrush1 = new CreateBrush(imagePGMBrush1);
//        createBrush1.createBrushs();
//        ImagePGM imagePGMSobel = new ImagePGM();
//        imagePGMSobel.ReadPGM("sobelMag.pgm");
//        PaintStrokes paintStrokes = new PaintStrokes(imagePPM,imagePGMSobel);
//        ImagePPM Backgroud = paintStrokes.PaintStrokes(density);
//        Backgroud.WritePPM("result.ppm");
        DifferenceOfGaussian differenceOfGaussian = new DifferenceOfGaussian(imagePPM);
        differenceOfGaussian.function();
    }
}