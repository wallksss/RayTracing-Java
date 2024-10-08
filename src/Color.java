import java.io.BufferedWriter;
import java.io.IOException;

public class Color extends Vec3 {
    public Color() {
        super();
    }

    public Color(float e0, float e1, float e2) {
        super(e0, e1, e2);
    }

    public void set(Color c) {
        e[0] = c.getX();
        e[1] = c.getY();
        e[2] = c.getZ();
    }
    
    public static float linearToGamma(float linear_component) {
        if(linear_component > 0)
            return (float) Math.sqrt(linear_component);
        return 0;
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
    public Color multiply(float t) {
        return new Color(getX() * t, getY() * t, getZ() * t);
    }

    @Override
    public Color divide(float t) {
        return multiply((float) (1.0 / t));
    }

    public static Color random() {
        return new Color(Util.randomFloat(), Util.randomFloat(), Util.randomFloat());
    }

    public static Color random(float min, float max) {
        return new Color(Util.randomFloat(min, max), Util.randomFloat(min, max), Util.randomFloat(min, max));
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

    public static Color multiply(Color v, float t) {
        return new Color(v.getX() * t, v.getY() * t, v.getZ() * t);
    }

    public static Color divide(Color v, float t) {
        return multiply(v, (float) (1.0 / t));
    }

    public static Color vecToColor(Vec3 v) {
        return new Color(v.getX(), v.getY(), v.getZ());
    }

    public int getRGB() {
        int r = (int) (e[0] * 255.0);
        int g = (int) (e[1] * 255.0);
        int b = (int) (e[2] * 255.0);

        return (r << 16) | (g << 8) | b;
    }
}
