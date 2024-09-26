public abstract class Material {
    public boolean scatter(Ray r_in, HitRecord rec, Color attenuation, Ray scattered) {
        return true;
    }
}
