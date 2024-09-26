import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Camera {
    public double aspect_ratio = 1.0;
    public int image_width = 100;
    public int samples_per_pixel = 10;
    public int max_depth = 10;
    private int image_height = 100;
    private double pixel_samples_scale;
    private static Vec3 cameraCenter;
    private static Vec3 pixel00;
    private static Vec3 pixelDeltaU;
    private static Vec3 pixelDeltaV;


    public void render(Hittable world) {
        initialize();

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("./imagem.ppm"));
            out.write("P3");
            out.newLine();
            out.write(image_width + " " + image_height);
            out.newLine();
            out.write("255");
            out.newLine();

            for(int j = 0; j < image_height; j++) {
                System.err.print("\rScanlines remaining: " + (image_height - j) + " ");
                System.err.flush();
                for(int i = 0; i < image_width; i++) {
                    Vec3 pixelColor = new Vec3(0, 0, 0);
                    for(int sample = 0; sample < samples_per_pixel; sample++) {
                        Ray r = getRay(i, j);
                        pixelColor = pixelColor.add(rayColor(r, max_depth, world));
                    }
                    writeColor(out, pixelColor.multiply(pixel_samples_scale)); //divide a cor do pixel pela quantidade de amostras
                    out.flush();
                }
            }
            System.err.print("Done\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        image_height = (int)(image_width / aspect_ratio);
        image_height = Math.max(image_height, 1);

        pixel_samples_scale = 1.0 / samples_per_pixel;

        cameraCenter = new Vec3(0, 0, 0);

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

        pixel00 = viewportUpperLeft
                .add(pixelDeltaU.add(pixelDeltaV).multiply(0.5));
    }

    private static Vec3 rayColor(Ray r, int depth, Hittable world) {
        if(depth <= 0)
            return new Vec3(0, 0, 0);

        HitRecord rec = new HitRecord();
        if(world.hit(r, new Interval(0.001, Util.INFINITY), rec)) {
            //Vec3 direction = Vec3.randomOnHemisphere(rec.normal); --> previous diffuse method
            Vec3 direction = rec.normal.add(Vec3.randomUnitVector()); //current diffuse method, which uses Lambertian Reflection
            return rayColor(new Ray(rec.p, direction), depth - 1, world).multiply(0.5);
        }

        Vec3 unitDirection = Vec3.unitVector(r.getDirection());
        Vec3 color1 = new Vec3(1.0, 1.0, 1.0);
        Vec3 color2 = new Vec3(0.5, 0.7, 1.0);
        double a = 0.5*(unitDirection.getY() + 1.0);
        return (color1
                .multiply(1.0 - a))
                .add(color2.multiply(a));
    }

    private static Ray getRay(int i, int j) { //constroi uma camera com direcao em um ponto aleatorio proximo a localizacao do pixel (i, j)
        Vec3 offset = sample_square();
        Vec3 pixel_sample = pixel00.
                add(pixelDeltaU.multiply(i + offset.getX())).
                add(pixelDeltaV.multiply(j + offset.getY()));

        Vec3 ray_origin = cameraCenter;
        Vec3 ray_direction = pixel_sample.subtract(ray_origin);

        return new Ray(ray_origin, ray_direction);
    }

    private static Vec3 sample_square() {
        return new Vec3(Util.randomDouble() - 0.5, Util.randomDouble() - 0.5, 0);
    }

    public static void writeColor(BufferedWriter out, Vec3 pixelColor) {
        double r = pixelColor.getX();
        double g = pixelColor.getY();
        double b = pixelColor.getZ();

        Interval intensity = new Interval(0.000, 0.999);
        int rbyte = (int)(256 * intensity.clamp(r));
        int gbyte = (int)(256 * intensity.clamp(g));
        int bbyte = (int)(256 * intensity.clamp(b));

        try {
            out.write(rbyte + " " + gbyte + " " + bbyte);
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
