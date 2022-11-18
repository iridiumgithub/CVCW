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
        ImagePGM imagePGMBrush2 = new ImagePGM();
        imagePGMBrush2.ReadPGM(brushFileNameTwo);
        Position[] positions = new Position[5];
        //Task1
        SobelEdgeMagnitudeMap sobelEdgeMagnitudeMap = new SobelEdgeMagnitudeMap(imagePPM);
        sobelEdgeMagnitudeMap.function().WritePGM("sobelMag.pgm");
        //Task2
        SobelEdgeOrientationMap sobelEdgeOrientationMap = new SobelEdgeOrientationMap(imagePPM);
        sobelEdgeOrientationMap.function().WritePGM("sobelDir.pgm");
        //Task3
        CreateBrush createBrush1 = new CreateBrush(imagePGMBrush1);
        createBrush1.createBrushs();
        //Task4
        ImagePGM imagePGMSobel = new ImagePGM();
        imagePGMSobel.ReadPGM("sobelMag.pgm");
        PaintStrokes paintStrokes = new PaintStrokes(imagePPM,imagePGMSobel);
        paintStrokes.PaintStrokes(density,positions);
        //Task5
        DifferenceOfGaussian differenceOfGaussian = new DifferenceOfGaussian(imagePPM);
        differenceOfGaussian.function();
//        Task6
        Improve improve = new Improve(imagePPM,imagePGMSobel);
        ImagePGM imagePGMthresh = new ImagePGM();
        imagePGMthresh.ReadPGM("DoGthresh.pgm");
        improve.function(imagePGMBrush2,imagePGMthresh,positions);
        //Task7
        ImagePPM result = new ImagePPM();
        result.ReadPPM("result.ppm");
        ImagePGM sobelOrientationMap = new ImagePGM();
        sobelOrientationMap.ReadPGM("sobelDir.pgm");
        Extend extend = new Extend(imagePPM,result,imagePGMSobel,sobelOrientationMap,imagePGMthresh);
        extend.function(imagePGMBrush1,imagePGMBrush2);
    }
}