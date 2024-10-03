import java.lang.Math;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Vec3 {
    protected float[] e;
    public Vec3(){
        e = new float[]{0, 0, 0};
    }
    public Vec3(float e0, float e1, float e2) {
        e = new float[]{e0, e1, e2};
    }
    public float getX() {
        return e[0];
    }
    public float getY() {
        return e[1];
    }
    public float getZ() {
        return e[2];
    }

    public Vec3 negate() {
        return new Vec3(-e[0], -e[1], -e[2]);
    }
    public float get(int i) {
        return e[i];
    }
    public void set(int i, float value) {
        e[i] = value;
    }
    public Vec3 add(Vec3 v) {
        return new Vec3(e[0] + v.e[0], e[1] + v.e[1], e[2] + v.e[2]);    }
    public Vec3 subtract(Vec3 v) {
        return new Vec3(e[0] - v.e[0], e[1] - v.e[1], e[2] - v.e[2]);
    }
    public Vec3 multiply(float t) {
        return new Vec3(e[0] * t, e[1] * t, e[2] * t);
    }
    public Vec3 divide(float t) {
        return multiply((float) (1.0 / t));
    }
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }
    public float lengthSquared() {
        return e[0]*e[0] + e[1]*e[1] + e[2]*e[2];
    }
    public boolean nearZero() {
        float s = 1e-8F;
        return (Math.abs(e[0]) < s) && (Math.abs(e[1]) < s) && (Math.abs(e[2]) < s);

    }

    public static Vec3 random() {
        return new Vec3(Util.randomFloat(), Util.randomFloat(), Util.randomFloat());
    }

    public static Vec3 random(float min, float max) {
        return new Vec3(Util.randomFloat(min, max), Util.randomFloat(min, max), Util.randomFloat(min, max));
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

    public static Vec3 multiply(Vec3 v, float t) {
        return new Vec3(v.e[0] * t, v.e[1] * t, v.e[2] * t);
    }

    public static Vec3 divide(Vec3 v, float t) {
        return multiply(v, (float) (1.0 /t));
    }

    public static float dot(Vec3 u, Vec3 v) {
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
            float lensq = p.lengthSquared();
            if(1e-160 < lensq && lensq <= 1) //first part deals with possible infinite vectors
                    return p.divide((float) Math.sqrt(lensq));
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

    public static Vec3 reflect(Vec3 v, Vec3 n) {
        return v.subtract(n.multiply(Vec3.dot(v, n) * 2));
    }

    public static Vec3 refract(Vec3 uv, Vec3 n, float etaiOverEtat) {
        float cosTheta = (float) Math.min(Vec3.dot(uv.negate(), n), 1.0);
        Vec3 rOutPerp = uv.add(n.multiply(cosTheta)).multiply(etaiOverEtat);
        Vec3 rOutParallel = n.multiply((float) -Math.sqrt(Math.abs(1.0 - rOutPerp.lengthSquared())));
        return rOutPerp.add(rOutParallel);
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES * 3).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putFloat(e[0]);
        buffer.putFloat(e[1]);
        buffer.putFloat(e[2]);
        return buffer.array();
    }
}


