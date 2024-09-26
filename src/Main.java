public class Main {
    public static void main(String[] args) {
        HittableList world = new HittableList();
        world.add(new Sphere(new Point3(0, 0, -1), 0.5));
        world.add(new Sphere(new Point3(0, -100.5, -1), 100));

        Camera camera = new Camera();

        camera.aspect_ratio = 16.0/9.0;
        camera.image_width = 400;
        camera.samples_per_pixel = 100;
        camera.max_depth = 50;

        camera.render(world);
    }
}