public class Main {
    public static void main(String[] args) {
        HittableList world = new HittableList();

        Camera camera = new Camera();
        camera.aspect_ratio = (float) (16.0 / 9.0);
        camera.image_width = 1280;
        camera.samples_per_pixel = 100; //quality
        camera.max_depth = 50; //quality
//        camera.samples_per_pixel = 50; //balanced
//        camera.max_depth = 25; //balanced
//        camera.samples_per_pixel = 10; //performance
//        camera.max_depth = 10; //performance
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