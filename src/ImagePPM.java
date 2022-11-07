import java.io.*;
import java.util.*;

public class ImagePPM {
    public int [][][] pixels;
    public int depth,width,height;

    public ImagePPM()
    {
        pixels = new int[1500][1500][3];
        depth = width = height = 0;
    }

    public ImagePPM(int inDepth, int inWidth, int inHeight)
    {
        pixels = new int[inWidth][inHeight][3];
        width = inWidth;
        height = inHeight;
        depth = inDepth;
    }

    public void ReadPPM(String fileName){
        String line;
        StringTokenizer st;

        try {
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(
                            new BufferedInputStream(
                                    new FileInputStream(fileName))));

            DataInputStream in2 =
                    new DataInputStream(
                            new BufferedInputStream(
                                    new FileInputStream(fileName)));

            // read PPM image header

            // skip comments
            line = in.readLine();
            in2.skip((line+"\n").getBytes().length);
            do {
                line = in.readLine();
                in2.skip((line+"\n").getBytes().length);
            } while (line.charAt(0) == '#');

            // the current line has dimensions
            st = new StringTokenizer(line);
            width = Integer.parseInt(st.nextToken());
            height = Integer.parseInt(st.nextToken());

            // next line has pixel depth
            line = in.readLine();
            in2.skip((line+"\n").getBytes().length);
            st = new StringTokenizer(line);
            depth = Integer.parseInt(st.nextToken());

            // read pixels now
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++)
                    for(int d = 0; d < 3;d++)
                        pixels[x][y][d] = in2.readUnsignedByte();

            in.close();
            in2.close();
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: image in "+fileName+" too big");
        } catch(FileNotFoundException e) {
            System.out.println("Error: file "+fileName+" not found");
        } catch(IOException e) {
            System.out.println("Error: end of stream encountered when reading "+fileName);
        }
    }


    public void WritePPM(String fileName)
    {
        String line;
        StringTokenizer st;
        int i;

        try {
            DataOutputStream out =
                    new DataOutputStream(
                            new BufferedOutputStream(
                                    new FileOutputStream(fileName)));

            out.writeBytes("P6\n");
            out.writeBytes("#created by WEI FU based on Paul Rosin\n");
            out.writeBytes(width+" "+height+"\n255\n");

            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++)
                    for(int d = 0; d < 3;d++)
                        out.writeByte((byte)pixels[x][y][d]);

            out.close();
        } catch(IOException e) {
            System.out.println("ERROR: cannot write output file");
        }
    }

    public void CopyPPM(ImagePPM imagePPM){
        depth = imagePPM.depth;
        width = imagePPM.width;
        height = imagePPM.height;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < 3; k++) {
                    pixels[i][j][k] = imagePPM.pixels[i][j][k];
                }
            }
        }
    }
}
