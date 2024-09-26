public class Ray {
    private final Vec3 origin;
    private final Vec3 direction;

    public Ray(Vec3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vec3 getOrigin() {
        return origin;
    }
    public Vec3 getDirection() {
        return direction;
    }
    public Vec3 at(double t) {
        return origin.add(direction.multiply(t));
    }
}
