public class Lambertian extends Material {
    private Color albedo;

    public Lambertian(Color albedo) {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, Color attenuation, Ray scattered){
        Vec3 scatter_direction = rec.normal.add(Vec3.randomUnitVector());

        if(scatter_direction.nearZero())
            scatter_direction = rec.normal;

        scattered.set(rec.p, scatter_direction);
        attenuation.set(albedo);
        return true;
    }
}
