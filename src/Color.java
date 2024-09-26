import java.io.BufferedWriter;
import java.io.IOException;

public class Color extends Vec3 {
    public Color() {
        super();
    }

    public Color(double e0, double e1, double e2) {
        super(e0, e1, e2);
    }

    public void set(Color c) {
        e[0] = c.getX();
        e[1] = c.getY();
        e[2] = c.getZ();
    }
    
    public static double linearToGamma(double linear_component) {
        if(linear_component > 0)
            return Math.sqrt(linear_component);
        return 0;
    }

    public static void writeColor(BufferedWriter out, Color pixelColor) {
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

    @Override
    public Color negate() {
        return new Color(-getX(), -getY(), -getZ());
    }

    @Override
    public Color add(Vec3 v) {
        return new Color(getX() + v.getX(), getY() + v.getY(), getZ() + v.getZ());
    }

    @Override
    public Color subtract(Vec3 v) {
        return new Color(getX() - v.getX(), getY() - v.getY(), getZ() - v.getZ());
    }

    @Override
    public Color multiply(double t) {
        return new Color(getX() * t, getY() * t, getZ() * t);
    }

    @Override
    public Color divide(double t) {
        return multiply(1.0 / t);
    }

    public static Color random() {
        return new Color(Util.randomDouble(), Util.randomDouble(), Util.randomDouble());
    }

    public static Color random(double min, double max) {
        return new Color(Util.randomDouble(min, max), Util.randomDouble(min, max), Util.randomDouble(min, max));
    }

    public static Color add(Color u, Color v) {
        return new Color(u.getX() + v.getX(), u.getY() + v.getY(), u.getZ() + v.getZ());
    }

    public static Color subtract(Color u, Color v) {
        return new Color(u.getX() - v.getX(), u.getY() - v.getY(), u.getZ() - v.getZ());
    }

    public static Color multiply(Color u, Color v) {
        return new Color(u.getX() * v.getX(), u.getY() * v.getY(), u.getZ() * v.getZ());
    }

    public static Color multiply(Color v, double t) {
        return new Color(v.getX() * t, v.getY() * t, v.getZ() * t);
    }

    public static Color divide(Color v, double t) {
        return multiply(v, 1.0 / t);
    }

    public static Color vecToColor(Vec3 v) {
        return new Color(v.getX(), v.getY(), v.getZ());
    }
}
