import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        HittableList world = new HittableList();

        Lambertian material_ground = new Lambertian(new Color(0.8, 0.8, 0.0));
        Lambertian material_center = new Lambertian(new Color(0.1, 0.2, 0.5));
        Dieletric material_left = new Dieletric(1.0 / 1.33);
        Metal material_right = new Metal(new Color(1, 1, 1), 0);

        world.add(new Sphere(new Point3(0.0, -100.5, -1.0), 100.0, material_ground));
        world.add(new Sphere(new Point3(0.0,    0.0, -1.2),   0.5, material_center));
        world.add(new Sphere(new Point3(-1.0,    0.0, -1.0),   0.5, material_left));
        world.add(new Sphere(new Point3(1.0,    0.0, -1.0),   0.5, material_right));

        Camera camera = new Camera();
        camera.aspect_ratio = 16.0 / 9.0;
        camera.image_width = 400;
        camera.samples_per_pixel = 1;
        camera.max_depth = 5;
        camera.view_fov = 60;
        camera.cameraCenter = new Point3(0, 0, 0);
        camera.v_up = new Vec3(0, 1, 0);
        camera.initialize();
        Renderer renderer = new Renderer(world, camera);

        Window window = new Window("RayTracing", camera.image_width, camera.image_height, renderer);
        Thread windowThread = new Thread(window);
        windowThread.start();
    }
}