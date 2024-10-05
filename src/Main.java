public class Main {
    public static void main(String[] args) {
        HittableList world = new HittableList();

        Lambertian material_ground = new Lambertian(new Color(1, 1, 0));
        Lambertian material_center = new Lambertian(new Color(0.8F, 0.2F, 0.2F));
        Dieletric material_left = new Dieletric((float) (2 / 3.0));
        Metal material_right = new Metal(new Color(1, 1, 1), 0.5f);

        world.add(new Sphere(new Point3(0.0f, -100.5f, -1.0f), 100.0f, material_ground));
        world.add(new Sphere(new Point3(0.0f,    0.0f, 1.2f),   0.5f, material_center));
        world.add(new Sphere(new Point3(-1.0f,    0.0f, 1.0f),   0.5f, material_left));
        world.add(new Sphere(new Point3(1.0f,    0.0f, 1.0f),   0.5f, material_right));

        Camera camera = new Camera();
        camera.aspect_ratio = (float) (16.0 / 9.0);
        camera.image_width = 1280;
        camera.samples_per_pixel = 1;
        camera.max_depth = 50;
        camera.view_fov = 90;
        camera.cameraCenter = new Point3(0, 0, 0);
        camera.v_up = new Vec3(0, 1, 0);
        camera.pitch = 180;
        camera.initialize();
        Renderer renderer = new Renderer(camera, world);
        Window window = new Window("RayTracing", camera.image_width, camera.image_height, camera);
        window.run();
        //Thread windowThread = new Thread(window);
        //windowThread.start();
    }
}