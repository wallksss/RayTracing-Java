public class Sphere {
    public static double     hit(Vec3 center, double radius, Ray r) {
        Vec3 oc = center.subtract(r.getOrigin());
        double a = Vec3.dot(r.getDirection(), r.getDirection());
        double b = -2 * Vec3.dot(r.getDirection(), oc);
        double c = Vec3.dot(oc, oc) - radius*radius;
        double discriminant = b*b - 4*a*c;

        if(discriminant < 0) {
            return -1.0;
        } else {
            return (-b - Math.sqrt(discriminant)) / (2 * a);
        }
    }
}

