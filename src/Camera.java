import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Camera {
    public double aspect_ratio = 1.0;
    public int image_width = 100;
    private int image_height = 100;
    private Vec3 cameraCenter;
    private Vec3 pixel00;
    private Vec3 pixelDeltaU;
    private Vec3 pixelDeltaV;


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
                    Vec3 pixelCenter = pixel00
                            .add(pixelDeltaU.multiply(i))
                            .add(pixelDeltaV.multiply(j));
                    Vec3 rayDirection = pixelCenter.subtract(cameraCenter);

                    Ray r = new Ray(cameraCenter, rayDirection);

                    Vec3 pixelColor = Camera.rayColor(r, world);
                    Util.writeColor(out, pixelColor);

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

    private static Vec3 rayColor(Ray r, Hittable world) {
        HitRecord rec = new HitRecord();
        if(world.hit(r, new Interval(0, Util.INFINITY), rec)) {
            return rec.normal.add(new Vec3(1, 1, 1)).multiply(0.5);
        }

        Vec3 unitDirection = Vec3.unitVector(r.getDirection());
        Vec3 color1 = new Vec3(1.0, 1.0, 1.0);
        Vec3 color2 = new Vec3(0.5, 0.7, 1.0);
        double a = 0.5*(unitDirection.getY() + 1.0);
        return (color1
                .multiply(1.0 - a))
                .add(color2.multiply(a));
    }

}
