public class Ray {
    private Vec3 origin;
    private Vec3 direction;

    public Ray() {
        this.origin = new Vec3();
        this.direction = new Vec3();
    }

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

    public static Vec3 rayColor(Ray r, Hittable world) {
        HitRecord rec = new HitRecord();
        if(world.hit(r, new Interval(0, Util.INFINITY), rec)) {
            return rec.normal.add(new Vec3(1, 1, 1)).multiply(0.5);
        }

        Vec3 unitDirection = Vec3.unitVector(r.getDirection());
        Vec3 color1 = new Vec3(1.0, 1.0, 1.0);
        Vec3 color2 = new Vec3(0.5, 0.7, 1.0);
        double a = 0.5*(unitDirection.getY() + 1.0);
        return (color1
                .multiply(1.0 - a))
                .add(color2.multiply(a));
    }
}
