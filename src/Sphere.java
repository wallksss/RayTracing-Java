public class Sphere extends Hittable {
    private final Point3 center;
    private final double radius;
    private Material material;

    public Sphere(Point3 center, double radius, Material material) {
        this.center = center;
        this.radius = Math.max(0, radius);
        this.material = material;
    }

    @Override
    public boolean hit(Ray r, Interval ray_t, HitRecord rec) {
        Vec3 oc = center.subtract(r.getOrigin());
        double a  = r.getDirection().lengthSquared();
        double h = Vec3.dot(r.getDirection(), oc);
        double c = oc.lengthSquared() - radius*radius;
        double discriminant = h*h - a*c;

        if(discriminant < 0) {
            return false;
        }

        double sqrtd = Math.sqrt(discriminant);

        double root = (h - sqrtd) / a;
        if(!ray_t.surrounds(root)) {
            root = (h + sqrtd) / a;
            if(!ray_t.surrounds(root)) {
                return false;
            }
        }

        rec.t = root;
        rec.p = r.at(rec.t);
        Vec3 outward_normal = rec.p.subtract(center).divide(radius);
        rec.set_face_normal(r, outward_normal);
        rec.material = this.material;

        return true;
    }
}

