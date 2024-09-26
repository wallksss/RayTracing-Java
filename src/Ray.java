public class Ray {
    private final Point3 origin;
    private final Vec3 direction;

    public Ray(Point3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Point3 getOrigin() {
        return origin;
    }
    public Vec3 getDirection() {
        return direction;
    }
    public Point3 at(double t) {
        return origin.add(direction.multiply(t));
    }
}
