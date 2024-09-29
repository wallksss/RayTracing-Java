import java.awt.image.BufferedImage;

public class Renderer {
    Camera camera;
    Hittable world;

    public Renderer(Hittable world, Camera camera) {
        this.world = world;
        this.camera = camera;
    }

    public void render(BufferedImage image) {
        for (int j = 0; j < camera.image_height; j++) {
            System.err.print("\rScanlines remaining: " + (camera.image_height - j) + " ");
            System.err.flush();
            for (int i = 0; i < camera.image_width; i++) {
                Color pixelColor = new Color(0, 0, 0);
                for (int sample = 0; sample < camera.samples_per_pixel; sample++) {
                    Ray r = getRay(i, j);
                    pixelColor = pixelColor.add(rayColor(r, camera.max_depth, world));
                }

                pixelColor = pixelColor.multiply(camera.pixel_samples_scale);
                image.setRGB(i, j, pixelColor.getRGB());
            }
        }
        System.err.print("Done\n");
    }

    private Ray getRay(int i, int j) { //builds a camera with direction at a random point close to the location of pixel (i, j)
        Vec3 offset = sample_square();
        Vec3 pixel_sample = camera.pixel00.
                add(camera.pixelDeltaU.multiply(i + offset.getX())).
                add(camera.pixelDeltaV.multiply(j + offset.getY()));

        Point3 ray_origin = camera.cameraCenter;
        Vec3 ray_direction = pixel_sample.subtract(ray_origin);

        return new Ray(ray_origin, ray_direction);
    }

    private Color rayColor(Ray r, int depth, Hittable world) {
        if(depth <= 0)
            return new Color(0, 0 ,0);

        HitRecord rec = new HitRecord();
        if(world.hit(r, new Interval(0.001, Util.INFINITY), rec)) {
            Ray scattered = new Ray();
            Color attenuation = new Color();
            //Vec3 direction = Vec3.randomOnHemisphere(rec.normal); --> previous diffuse method
            if(rec.material != null && rec.material.scatter(r, rec, attenuation, scattered)) {
                return Color.multiply(attenuation, (rayColor(scattered, depth - 1, world)));
            }
            return new Color(0, 0 ,0);
        }

        Vec3 unitDirection = Vec3.unitVector(r.getDirection());
        Color color1 = new Color(1.0, 1.0, 1.0);
        Color color2 = new Color(0.5, 0.7, 1.0);
        double a = 0.5*(unitDirection.getY() + 1.0);
        return (color1
                .multiply(1.0 - a))
                .add(color2.multiply(a));
    }

    private static Vec3 sample_square() {
        return new Vec3(Util.randomDouble() - 0.5, Util.randomDouble() - 0.5, 0);
    }

    public Camera getCamera() {
        return camera;
    }
}
