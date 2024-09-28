import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Camera {
    public double aspect_ratio = 1.0;
    public int image_width = 100;
    public int samples_per_pixel = 10;
    public int max_depth = 10;
    public int image_height = 100;
    private double pixel_samples_scale;
    private static Point3 cameraCenter;
    private static Point3 pixel00;
    private static Vec3 pixelDeltaU;
    private static Vec3 pixelDeltaV;

    //    public void render(Hittable world) {
//        initialize();
//
//        try {
//            BufferedWriter out = new BufferedWriter(new FileWriter("./imagem.ppm"));
//            out.write("P3");
//            out.newLine();
//            out.write(image_width + " " + image_height);
//            out.newLine();
//            out.write("255");
//            out.newLine();
//
//            for(int j = 0; j < image_height; j++) {
//                System.err.print("\rScanlines remaining: " + (image_height - j) + " ");
//                System.err.flush();
//                for(int i = 0; i < image_width; i++) {
//                    Color pixelColor = new Color(0, 0, 0);
//                    for(int sample = 0; sample < samples_per_pixel; sample++) {
//                        Ray r = getRay(i, j);
//                        pixelColor = pixelColor.add(rayColor(r, max_depth, world));
//                    }
//                    Color.writeColor(out, pixelColor.multiply(pixel_samples_scale)); //divides pixel color by the sample amount
//                    out.flush();
//                }
//            }
//            System.err.print("Done\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void render(Hittable world, Window currentWindow) {
        initialize();
        currentWindow.initialize();

        for (int j = 0; j < image_height; j++) {
            System.err.print("\rScanlines remaining: " + (image_height - j) + " ");
            System.err.flush();
            for (int i = 0; i < image_width; i++) {
                Color pixelColor = new Color(0, 0, 0);
                for (int sample = 0; sample < samples_per_pixel; sample++) {
                    Ray r = getRay(i, j);
                    pixelColor = pixelColor.add(rayColor(r, max_depth, world));
                }

                pixelColor = pixelColor.multiply(pixel_samples_scale);
                currentWindow.setPixel(i, j, pixelColor);
            }
        }
        System.err.print("Done\n");

        currentWindow.repaint();
    }

    private void initialize() {
        image_height = (int)(image_width / aspect_ratio);
        image_height = Math.max(image_height, 1);

        pixel_samples_scale = 1.0 / samples_per_pixel;

        cameraCenter = new Point3(0, 0, 0);

        double focalLength = 1.0;
        double viewport_height = 2.0;
        double viewport_width = viewport_height * ((double)image_width /image_height);

        Vec3 viewportU = new Vec3(viewport_width, 0, 0);
        Vec3 viewportV = new Vec3(0, -viewport_height, 0);

        pixelDeltaU = viewportU.divide(image_width);
        pixelDeltaV = viewportV.divide(image_height);

        Vec3 viewportUpperLeft = cameraCenter
                .subtract(new Vec3(0, 0, focalLength))
                .subtract(viewportU.divide(2))
                .subtract(viewportV.divide(2));

        pixel00 = Point3.vecToPoint(viewportUpperLeft
                .add(pixelDeltaU.add(pixelDeltaV).multiply(0.5)));
    }

    private static Color rayColor(Ray r, int depth, Hittable world) {
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

    private static Ray getRay(int i, int j) { //builds a camera with direction at a random point close to the location of pixel (i, j)
        Vec3 offset = sample_square();
        Vec3 pixel_sample = pixel00.
                add(pixelDeltaU.multiply(i + offset.getX())).
                add(pixelDeltaV.multiply(j + offset.getY()));

        Point3 ray_origin = cameraCenter;
        Vec3 ray_direction = pixel_sample.subtract(ray_origin);

        return new Ray(ray_origin, ray_direction);
    }

    private static Vec3 sample_square() {
        return new Vec3(Util.randomDouble() - 0.5, Util.randomDouble() - 0.5, 0);
    }
}
