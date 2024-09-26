import java.io.BufferedWriter;
import java.io.IOException;

public class Color extends Vec3 {
    public static double linearToGamma(double linear_component) {
        if(linear_component > 0)
            return Math.sqrt(linear_component);
        return 0;
    }

    public static void writeColor(BufferedWriter out, Vec3 pixelColor) {
        double r = pixelColor.getX();
        double g = pixelColor.getY();
        double b = pixelColor.getZ();

        r = linearToGamma(r);
        g = linearToGamma(g);
        b = linearToGamma(b);

        Interval intensity = new Interval(0.000, 0.999);
        int rbyte = (int)(256 * intensity.clamp(r));
        int gbyte = (int)(256 * intensity.clamp(g));
        int bbyte = (int)(256 * intensity.clamp(b));

        try {
            out.write(rbyte + " " + gbyte + " " + bbyte);
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
