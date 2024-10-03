import java.util.Random;

public class Util {
    public static final float INFINITY = Float.POSITIVE_INFINITY;
    public static final float NEGATIVE_INFINITY = Float.NEGATIVE_INFINITY;
    public static final float PI = 3.1415926535897932385f;
    private static final Random random = new Random();

    public static float randomFloat() {
        return random.nextFloat();
    }

    public static float randomFloat(float min, float max) {
        return min + (max - min) * randomFloat();
    }

    public static float degreesToRadians(float degrees) {
        return degrees * PI / 180.0f;
    }
}
