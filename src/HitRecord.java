public class HitRecord {
    public Point3 p;
    public Vec3 normal;
    public Material material;
    public float t;
    public boolean front_face;

    void set_face_normal(Ray r, Vec3 outward_normal) {
        front_face = Vec3.dot(r.getDirection(), outward_normal) < 0;
        normal = front_face ? outward_normal : outward_normal.negate();
    }
}
