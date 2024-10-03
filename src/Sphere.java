public class Sphere extends Hittable {
    private final Point3 center;
    private final float radius;
    private Material material;

    public Sphere(Point3 center, float radius, Material material) {
        this.center = center;
        this.radius = Math.max(0, radius);
        this.material = material;
    }

    @Override
    public boolean hit(Ray r, Interval ray_t, HitRecord rec) {
        Vec3 oc = center.subtract(r.getOrigin());
        float a  = r.getDirection().lengthSquared();
        float h = Vec3.dot(r.getDirection(), oc);
        float c = oc.lengthSquared() - radius*radius;
        float discriminant = h*h - a*c;

        if(discriminant < 0) {
            return false;
        }

        float sqrtd = (float) Math.sqrt(discriminant);

        float root = (h - sqrtd) / a;
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

