public class HitRecord {
    public Vec3 p;
    public Vec3 normal;
    public double t;
    public boolean front_face;

    void set_face_normal(Ray r, Vec3 outward_normal) {
        front_face = Vec3.dot(r.getDirection(), outward_normal) < 0;
        normal = front_face ? outward_normal : outward_normal.negate();
    }
}
