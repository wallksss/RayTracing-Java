public class Camera {
    public double aspect_ratio = 1.0;
    public int image_width = 100;
    public int image_height = 100;
    public double view_fov = 90;
    public Vec3 direction = new Vec3(0, 0, -1); // Nova direção da câmera
    public Vec3 v_up = new Vec3(0, 1, 0);       // Vetor de "cima"
    public int samples_per_pixel = 10;
    public int max_depth = 10;

    public double pixel_samples_scale;
    public Point3 cameraCenter;
    public Point3 pixel00;
    public Vec3 pixelDeltaU;
    public Vec3 pixelDeltaV;
    private static Vec3 u, v, w;

    public void initialize() {
        image_height = (int) (image_width / aspect_ratio);
        image_height = Math.max(image_height, 1);

        pixel_samples_scale = 1.0 / samples_per_pixel;

        cameraCenter = cameraCenter;  // Posiciona a câmera

        double focalLength = direction.length(); // Comprimento focal agora baseado na direção
        double theta = Math.toRadians(view_fov);
        double h = Math.tan(theta / 2);
        double viewport_height = 2 * h * focalLength;
        double viewport_width = viewport_height * ((double) image_width / image_height);

        // Direção é usada em vez de `look_at`
        w = Vec3.unitVector(direction.negate()); // Vetor w agora é baseado na direção
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

    public void setCameraDirection(double x, double y, double z) {
        direction = direction.add(new Vec3(x, y, z));
        initialize();
    }

    public void setCameraCenter(double x, double y, double z) {
        cameraCenter.e[0] += x;
        cameraCenter.e[1] += y;
        cameraCenter.e[2] += z;
        initialize();
    }
}
