public class Sphere {
    public static double     hit(Vec3 center, double radius, Ray r) {
        Vec3 oc = center.subtract(r.getOrigin());
        double a  = r.getDirection().lengthSquared();
        double h = Vec3.dot(r.getDirection(), oc);
        double c = oc.lengthSquared() - radius*radius;
        double discriminant = h*h - a*c;

        if(discriminant < 0) {
            return -1.0;
        } else {
            return (h - Math.sqrt(discriminant)) / a;
        }
    }
}

