import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Render {
    void renderImage(int imgWidth, int imgHeight, Vec3 pixel00, Vec3 pixelDeltaU, Vec3 pixelDeltaV, Vec3 cameraCenter) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("./imagem.ppm"));
            out.write("P3");
            out.newLine();
            out.write(imgWidth + " " + imgHeight);
            out.newLine();
            out.write("255");
            out.newLine();

            for(int j = 0; j < imgHeight; j++) {
                System.err.print("\rScanlines remaining: " + (imgHeight - j) + " ");
                System.err.flush();
                for(int i = 0; i < imgWidth; i++) {
                    Vec3 pixelCenter = pixel00
                            .add(pixelDeltaU.multiply(i))
                            .add(pixelDeltaV.multiply(j));
                    Vec3 rayDirection = pixelCenter.subtract(cameraCenter);

                    Ray r = new Ray(cameraCenter, rayDirection);

                    Vec3 pixelColor = Ray.rayColor(r);
                    Util.writeColor(out, pixelColor);

                    out.flush();
                }
            }
            System.err.print("Done\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}