public class Dieletric extends Material {
    private float refractionIndex;

    public Dieletric(float refractionIndex) {
        this.refractionIndex = refractionIndex;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, Color attenuation, Ray scattered) {
        attenuation.set(new Color(1.0F, 1.0F, 1.0F)); //attenuation is always 1, glass surface absorbs nothing
        float ri = (float) (rec.front_face ? (1.0 / refractionIndex) : refractionIndex);

        Vec3 unitDirection = Vec3.unitVector(r_in.getDirection());

        float cosTheta = (float) Math.min(Vec3.dot(unitDirection.negate(), rec.normal), 1.0);
        float sinTheta = (float) Math.sqrt(1.0 - cosTheta * cosTheta);

        boolean cannotRefract = ri * sinTheta > 1.0;
        Vec3 direction;

        if(cannotRefract || reflectance(cosTheta, ri) > Util.randomFloat())
            direction = Vec3.reflect(unitDirection, rec.normal);
        else
            direction = Vec3.refract(unitDirection, rec.normal, ri);

        scattered.set(rec.p, direction);
        return true;
    }

    private static float reflectance(float cos, float refractionIndex) {
        float r0 = (1 - refractionIndex) / (1 + refractionIndex);
        r0 = r0 * r0;
        return (float) (r0 + (1 - r0) * Math.pow((1 - cos), 5));
    }
}
