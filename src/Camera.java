import org.jocl.Sizeof;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.XMLFormatter;

public class Camera {
    public float aspect_ratio = 1.0f;
    public int image_width = 100;
    public int image_height = 100;
    public float view_fov = 90;
    public Vec3 direction;
    public Vec3 v_up = new Vec3(0, 1, 0);
    public int samples_per_pixel = 10;
    public int max_depth = 10;

    public float pixel_samples_scale;
    public Point3 cameraCenter;
    public Point3 pixel00;
    public Vec3 pixelDeltaU;
    public Vec3 pixelDeltaV;
    private static Vec3 u, v, w;

    public float pitch = 0;
    public float yaw = 0;

    public void initialize() {
        float x = (float) (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)));
        float y = (float) Math.sin(Math.toRadians(pitch));
        float z = (float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)));
        direction = Vec3.unitVector(new Vec3(x, y, z));

        image_height = (int) (image_width / aspect_ratio);
        image_height = Math.max(image_height, 1);

        pixel_samples_scale = (float) (1.0 / samples_per_pixel);

        float focalLength = direction.length();
        float theta = (float) Math.toRadians(view_fov);
        float h = (float) Math.tan(theta / 2);
        float viewport_height = 2 * h * focalLength;
        float viewport_width = viewport_height * ((float) image_width / image_height);

        w = Vec3.unitVector(direction.negate());
        u = Vec3.unitVector(Vec3.cross(v_up, w));
        v = Vec3.cross(w, u);

        Vec3 viewportU = u.multiply(viewport_width);
        Vec3 viewportV = v.negate().multiply(viewport_height);

        pixelDeltaU = viewportU.divide(image_width);
        pixelDeltaV = viewportV.divide(image_height);

        Vec3 viewportUpperLeft = cameraCenter
                .subtract(w.multiply(focalLength))
                .subtract(viewportU.divide(2))
                .subtract(viewportV.divide(2));

        pixel00 = Point3.vecToPoint(viewportUpperLeft
                .add(pixelDeltaU.add(pixelDeltaV).multiply(0.5F)));
    }


    public void rotateCamera(float deltaPitch, float deltaYaw) {
        pitch += deltaPitch;
        yaw += deltaYaw;
        initialize();
    }

    public void setCameraCenter(float x, float y, float z) {
        Vec3 xAxis = Vec3.unitVector(Vec3.cross(v_up, direction));
        Vec3 yAxis = Vec3.unitVector(v_up);
        Vec3 zAxis = Vec3.unitVector(direction);

        cameraCenter = cameraCenter.add(xAxis.multiply(x));
        cameraCenter = cameraCenter.add(yAxis.multiply(y));
        cameraCenter = cameraCenter.add(zAxis.multiply(z));

        initialize();
    }

    public int getCameraSize() {
        return Float.BYTES + // aspect_ratio
                Integer.BYTES * 2 + // image_width, image_height
                Float.BYTES + // view_fov
                Float.BYTES * 3 + // direction (float3)
                Float.BYTES * 3 + // v_up (float3)
                Integer.BYTES * 2 + // samples_per_pixel, max_depth
                Float.BYTES + // pixel_samples_scale
                Float.BYTES * 3 + // camera_center (float3)
                Float.BYTES * 3 + // pixel00 (float3)
                Float.BYTES * 3 + // pixelDeltaU (float3)
                Float.BYTES * 3 + // pixelDeltaV (float3)
                Float.BYTES * 3 + // u (float3)
                Float.BYTES * 3 + // v (float3)
                Float.BYTES * 3 + // w (float3)
                Float.BYTES + // pitch
                Float.BYTES; // yaw
    }

    public ByteBuffer toByteBuffer(int bufferSize) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putFloat(aspect_ratio);
        buffer.putInt(image_width);
        buffer.putInt(image_height);
        buffer.putFloat(view_fov);
        buffer.put(direction.toByteArray());
        buffer.put(v_up.toByteArray());
        buffer.putInt(samples_per_pixel);
        buffer.putInt(max_depth);
        buffer.putFloat(pixel_samples_scale);
        buffer.put(cameraCenter.toByteArray());
        buffer.put(pixel00.toByteArray());
        buffer.put(pixelDeltaU.toByteArray());
        buffer.put(pixelDeltaV.toByteArray());
        buffer.put(u.toByteArray());
        buffer.put(v.toByteArray());
        buffer.put(w.toByteArray());
        buffer.putFloat(pitch);
        buffer.putFloat(yaw);
        buffer.flip();
        return buffer;
    }
}
