public class Dieletric extends Material {
    private double refractionIndex;

    public Dieletric(double refractionIndex) {
        this.refractionIndex = refractionIndex;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, Color attenuation, Ray scattered) {
        attenuation.set(new Color(1.0, 1.0, 1.0)); //attenuation is always 1, glass surface absorbs nothing
        double ri = rec.front_face ? (1.0 / refractionIndex) : refractionIndex;

        Vec3 unitDirection = Vec3.unitVector(r_in.getDirection());

        double cosTheta = Math.min(Vec3.dot(unitDirection.negate(), rec.normal), 1.0);
        double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);

        boolean cannotRefract = ri * sinTheta > 1.0;
        Vec3 direction;

        if(cannotRefract || reflectance(cosTheta, ri) > Util.randomDouble())
            direction = Vec3.reflect(unitDirection, rec.normal);
        else
            direction = Vec3.refract(unitDirection, rec.normal, ri);

        scattered.set(rec.p, direction);
        return true;
    }

    private static double reflectance(double cos, double refractionIndex) {
        double r0 = (1 - refractionIndex) / (1 + refractionIndex);
        r0 = r0 * r0;
        return r0 + (1 - r0) * Math.pow((1 - cos), 5);
    }
}
