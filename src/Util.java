import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Util {
    public static void writeColor(Vec3 pixelColor) {
        double r = pixelColor.getX();
        double g = pixelColor.getY();
        double b = pixelColor.getZ();

        int rbyte = (int)(255.999 * r);
        int gbyte = (int)(255.999 * g);
        int bbyte = (int)(255.999 * b);

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("../imagem.ppm"));
            out.write(rbyte + " " + gbyte + " " + bbyte);
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
