public class Point3 extends Vec3 {
    public Point3() {
        super();
    }

    public Point3(float e0, float e1, float e2) {
        super(e0, e1, e2);
    }

    @Override
    public Point3 negate() {
        return new Point3(-getX(), -getY(), -getZ());
    }

    @Override
    public Point3 add(Vec3 v) {
        return new Point3(getX() + v.getX(), getY() + v.getY(), getZ() + v.getZ());
    }

    @Override
    public Point3 subtract(Vec3 v) {
        return new Point3(getX() - v.getX(), getY() - v.getY(), getZ() - v.getZ());
    }

    @Override
    public Point3 multiply(float t) {
        return new Point3(getX() * t, getY() * t, getZ() * t);
    }

    @Override
    public Point3 divide(float t) {
        return multiply((float) (1.0 / t));
    }

    public static Point3 random() {
        return new Point3(Util.randomFloat(), Util.randomFloat(), Util.randomFloat());
    }

    public static Point3 random(float min, float max) {
        return new Point3(Util.randomFloat(min, max), Util.randomFloat(min, max), Util.randomFloat(min, max));
    }

    public static Point3 add(Point3 u, Point3 v) {
        return new Point3(u.getX() + v.getX(), u.getY() + v.getY(), u.getZ() + v.getZ());
    }

    public static Point3 subtract(Point3 u, Point3 v) {
        return new Point3(u.getX() - v.getX(), u.getY() - v.getY(), u.getZ() - v.getZ());
    }

    public static Point3 multiply(Point3 u, Point3 v) {
        return new Point3(u.getX() * v.getX(), u.getY() * v.getY(), u.getZ() * v.getZ());
    }

    public static Point3 multiply(Point3 v, float t) {
        return new Point3(v.getX() * t, v.getY() * t, v.getZ() * t);
    }

    public static Point3 divide(Point3 v, float t) {
        return multiply(v, (float) (1.0 / t));
    }

    public static Point3 vecToPoint(Vec3 v) {
        return new Point3(v.getX(), v.getY(), v.getZ());
    }
}
