public class Ray {
    private Point3 origin;
    private Vec3 direction;

    public Ray() {
        origin = new Point3(0, 0, 0);
        direction = new Vec3(0, 0, 0);
    }

    public Ray(Point3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public void set(Point3 origin, Vec3 direction) {
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
