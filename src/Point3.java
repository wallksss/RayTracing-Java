public class Point3 extends Vec3 {
    public Point3() {
        super();
    }

    public Point3(double e0, double e1, double e2) {
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
    public Point3 multiply(double t) {
        return new Point3(getX() * t, getY() * t, getZ() * t);
    }

    @Override
    public Point3 divide(double t) {
        return multiply(1.0 / t);
    }

    public static Point3 random() {
        return new Point3(Util.randomDouble(), Util.randomDouble(), Util.randomDouble());
    }

    public static Point3 random(double min, double max) {
        return new Point3(Util.randomDouble(min, max), Util.randomDouble(min, max), Util.randomDouble(min, max));
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

    public static Point3 multiply(Point3 v, double t) {
        return new Point3(v.getX() * t, v.getY() * t, v.getZ() * t);
    }

    public static Point3 divide(Point3 v, double t) {
        return multiply(v, 1.0 / t);
    }

    public static Point3 vecToPoint(Vec3 v) {
        return new Point3(v.getX(), v.getY(), v.getZ());
    }
}
