import java.util.logging.XMLFormatter;

public class Camera {
    public double aspect_ratio = 1.0;
    public int image_width = 100;
    public int image_height = 100;
    public double view_fov = 90;
    public Vec3 direction;
    public Vec3 v_up = new Vec3(0, 1, 0);
    public int samples_per_pixel = 10;
    public int max_depth = 10;

    public double pixel_samples_scale;
    public Point3 cameraCenter;
    public Point3 pixel00;
    public Vec3 pixelDeltaU;
    public Vec3 pixelDeltaV;
    private static Vec3 u, v, w;

    private double pitch = 0;
    private double yaw = 0;

    public void initialize() {
        double x = Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
        double y = Math.sin(Math.toRadians(pitch));
        double z = Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
        direction = Vec3.unitVector(new Vec3(x, y, z));

        image_height = (int) (image_width / aspect_ratio);
        image_height = Math.max(image_height, 1);

        pixel_samples_scale = 1.0 / samples_per_pixel;

        double focalLength = direction.length();
        double theta = Math.toRadians(view_fov);
        double h = Math.tan(theta / 2);
        double viewport_height = 2 * h * focalLength;
        double viewport_width = viewport_height * ((double) image_width / image_height);

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
                .add(pixelDeltaU.add(pixelDeltaV).multiply(0.5)));
    }


    public void rotateCamera(double deltaPitch, double deltaYaw) {
        pitch += deltaPitch;
        yaw += deltaYaw;
        initialize();
    }

    public void setCameraCenter(double x, double y, double z) {
        Vec3 xAxis = Vec3.unitVector(Vec3.cross(v_up, direction));
        Vec3 yAxis = Vec3.unitVector(v_up);
        Vec3 zAxis = Vec3.unitVector(direction);

        cameraCenter = cameraCenter.add(xAxis.multiply(x));
        cameraCenter = cameraCenter.add(yAxis.multiply(y));
        cameraCenter = cameraCenter.add(zAxis.multiply(z));

        initialize();
    }
}
