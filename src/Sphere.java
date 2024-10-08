public class Sphere{
    private final Point3 center;
    private final float radius;
    //private Material material;

    public Sphere(Point3 center, float radius) {
        this.center = center;
        this.radius = Math.max(0, radius);
    }
}

