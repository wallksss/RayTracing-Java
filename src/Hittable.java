public abstract class Hittable {
    public abstract boolean hit(Ray r, Interval ray_t, HitRecord rec);
}
