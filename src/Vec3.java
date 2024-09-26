import java.lang.Math;
public class Vec3 {
    private double[] e;
    public Vec3(){
        e = new double[]{0, 0, 0};
    }
    public Vec3(double e0, double e1, double e2) {
        e = new double[]{e0, e1, e2};
    }
    public double getX() {
        return e[0];
    }
    public double getY() {
        return e[1];
    }
    public double getZ() {
        return e[2];
    }

    public Vec3 negate() {
        return new Vec3(-e[0], -e[1], -e[2]);
    }
    public double get(int i) {
        return e[i];
    }
    public void set(int i, double value) {
        e[i] = value;
    }
    public Vec3 add(Vec3 v) {
        return new Vec3(e[0] + v.e[0], e[1] + v.e[1], e[2] + v.e[2]);    }
    public Vec3 subtract(Vec3 v) {
        return new Vec3(e[0] - v.e[0], e[1] - v.e[1], e[2] - v.e[2]);
    }
    public Vec3 multiply(double t) {
        return new Vec3(e[0] * t, e[1] * t, e[2] * t);
    }
    public Vec3 divide(double t) {
        return multiply(1.0 / t);
    }
    public double length() {
        return Math.sqrt(lengthSquared());
    }
    public double lengthSquared() {
        return e[0]*e[0] + e[1]*e[1] + e[2]*e[2];
    }

    public static Vec3 random() {
        return new Vec3(Util.randomDouble(), Util.randomDouble(), Util.randomDouble());
    }

    public static Vec3 random(double min, double max) {
        return new Vec3(Util.randomDouble(min, max), Util.randomDouble(min, max), Util.randomDouble(min, max));
    }

    public static void printVec3(Vec3 v) {
        System.out.println(v.e[0] + " " + v.e[1] + " " + v.e[2]);
    }

    public static Vec3 add(Vec3 u, Vec3 v) {
        return new Vec3(u.e[0] + v.e[0], u.e[1] + v.e[1], u.e[2] + v.e[2]);
    }

    public static Vec3 subtract(Vec3 u, Vec3 v) {
        return new Vec3(u.e[0] - v.e[0], u.e[1] - v.e[1], u.e[2] - v.e[2]);
    }

    public static Vec3 multiply(Vec3 u, Vec3 v) {
        return new Vec3(u.e[0] * v.e[0], u.e[1] * v.e[1], u.e[2] * v.e[2]);
    }

    public static Vec3 multiply(Vec3 v, double t) {
        return new Vec3(v.e[0] * t, v.e[1] * t, v.e[2] * t);
    }

    public static Vec3 divide(Vec3 v, double t) {
        return multiply(v, 1.0 /t);
    }

    public static double dot(Vec3 u, Vec3 v) {
        return (u.e[0] * v.e[0]) + (u.e[1] * v.e[1]) + (u.e[2] * v.e[2]);
    }

    public static Vec3 cross(Vec3 u, Vec3 v) {
        return new Vec3(u.e[1] * v.e[2] - u.e[2] * v.e[1],
                        u.e[2] * v.e[0] - u.e[0] * v.e[2],
                        u.e[0] * v.e[1] - u.e[1] * v.e[0]);
    }

    public static Vec3 unitVector(Vec3 v) {
        return divide(v, v.length());
    }

    public static Vec3 randomUnitVector() {
        while(true) {
            Vec3 p = random(-1, 1);
            double lensq = p.lengthSquared();
            if(1e-160 < lensq && lensq <= 1) //first part deals with possible infinite vectors
                    return p.divide(Math.sqrt(lensq));
        }
    }

    public static Vec3 pointToVec(Point3 p) {
        return new Vec3(p.getX(), p.getY(), p.getZ());
    }

    public static Vec3 randomOnHemisphere(Vec3 normal) {
        Vec3 onUnitSphere = randomUnitVector();
        if(Vec3.dot(onUnitSphere, normal) > 0.0)
            return onUnitSphere;
        else
            return onUnitSphere.negate();
    }
}


