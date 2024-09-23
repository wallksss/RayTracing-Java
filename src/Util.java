import java.io.BufferedWriter;
import java.io.IOException;

public class Util {
    public static final double INFINITY = Double.POSITIVE_INFINITY;
    public static final double NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;
    public static final double PI = 3.1415926535897932385;

    public static double degreesToRadians(double degrees) {
        return degrees * PI / 180.0;
    }

    public static void writeColor(BufferedWriter out, Vec3 pixelColor) {
        double r = pixelColor.getX();
        double g = pixelColor.getY();
        double b = pixelColor.getZ();

        int rbyte = (int)(255.999 * r);
        int gbyte = (int)(255.999 * g);
        int bbyte = (int)(255.999 * b);

        try {
            out.write(rbyte + " " + gbyte + " " + bbyte);
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
