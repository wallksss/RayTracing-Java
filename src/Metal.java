    public class Metal extends Material {
        private Color albedo;
        private float fuzz;

        public Metal(Color albedo, float fuzz) {
            this.albedo = albedo;
            this.fuzz = fuzz < 1 ? fuzz : 1;
        }

        @Override
        public boolean scatter(Ray r_in, HitRecord rec, Color attenuation, Ray scattered){
            Vec3 reflected = Vec3.reflect(r_in.getDirection(), rec.normal);
            reflected = Vec3.unitVector(reflected).add(Vec3.randomUnitVector().multiply(fuzz));

            scattered.set(rec.p, reflected);
            attenuation.set(albedo);

            return (Vec3.dot(scattered.getDirection(), rec.normal) > 0);
        }
    }
