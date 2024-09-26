import java.util.Random;

public class Util {
    public static final double INFINITY = Double.POSITIVE_INFINITY;
    public static final double NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;
    public static final double PI = 3.1415926535897932385;
    private static final Random random = new Random();

    public static double randomDouble() {
        return random.nextDouble();
    }

    public static double randomDouble(double min, double max) {
        return min + (max - min) * randomDouble();
    }

    public static double degreesToRadians(double degrees) {
        return degrees * PI / 180.0;
    }
}
