public abstract class Hittable {
    public abstract boolean hit(Ray r, double rayTMin, double rayTMax, HitRecord rec);
}
