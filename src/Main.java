import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        HittableList world = new HittableList();

//        Lambertian material_ground = new Lambertian(new Color(0.2, 0.5, 0.3));
//        Lambertian material_red = new Lambertian(new Color(0.9, 0.1, 0.1));
//        Lambertian material_blue = new Lambertian(new Color(0.1, 0.1, 0.9));
//        Lambertian material_yellow = new Lambertian(new Color(0.9, 0.9, 0.1));
//        Lambertian material_purple = new Lambertian(new Color(0.5, 0.2, 0.5));
//        Metal material_metal1 = new Metal(new Color(0.8, 0.6, 0.2), 0.1);
//        Metal material_metal2 = new Metal(new Color(0.5, 0.5, 0.5), 0.3);
//        Dieletric material_glass1 = new Dieletric(1.5);
//        Dieletric material_glass2 = new Dieletric(1.3);
//
//        world.add(new Sphere(new Point3(0.0, -100.5, -1.0), 100.0, material_ground));
//        world.add(new Sphere(new Point3(-3.0, 0.0, -2.0), 0.5, material_red));
//        world.add(new Sphere(new Point3(-2.0, 1.0, -1.5), 0.75, material_blue));
//        world.add(new Sphere(new Point3(-1.0, -0.5, -1.0), 0.6, material_yellow));
//        world.add(new Sphere(new Point3(1.0, 0.5, -1.0), 0.4, material_purple));
//        world.add(new Sphere(new Point3(2.0, -1.0, -1.5), 0.8, material_metal1));
//        world.add(new Sphere(new Point3(3.0, 0.0, -2.0), 0.6, material_metal2));
//        world.add(new Sphere(new Point3(-1.5, -1.0, -1.5), 0.5, material_glass1));
//        world.add(new Sphere(new Point3(1.5, 1.0, -2.5), 0.5, material_glass2));
//        world.add(new Sphere(new Point3(0.0, 1.5, -2.0), 0.7, material_red));
//        world.add(new Sphere(new Point3(-2.5, -0.5, -1.5), 0.4, material_blue));
//        world.add(new Sphere(new Point3(2.5, 0.5, -1.5), 0.5, material_yellow));
//        world.add(new Sphere(new Point3(-1.0, 2.0, -3.0), 0.3, material_purple));
//        world.add(new Sphere(new Point3(1.0, -2.0, -1.5), 0.4, material_metal1));
//        world.add(new Sphere(new Point3(-3.0, 2.0, -3.0), 0.6, material_glass2));
//        world.add(new Sphere(new Point3(0.5, -3.0, -1.0), 0.8, material_metal2));
//        world.add(new Sphere(new Point3(-1.5, -3.0, -1.0), 0.5, material_red));
//        world.add(new Sphere(new Point3(1.5, 3.0, -2.0), 0.3, material_blue));
//        world.add(new Sphere(new Point3(-2.0, 3.0, -1.5), 0.4, material_yellow));
//        world.add(new Sphere(new Point3(2.0, 1.5, -1.0), 0.6, material_purple));
//        world.add(new Sphere(new Point3(-3.0, -1.5, -2.0), 0.5, material_glass1));
//        world.add(new Sphere(new Point3(3.0, -1.0, -3.0), 0.7, material_glass2));
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
        camera.image_width = 1000;
        camera.samples_per_pixel = 100;
        camera.max_depth = 50;

        Window window = new Window();

        JFrame f = new JFrame("Janela");
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(camera.image_width, (int)(camera.image_width / camera.aspect_ratio)));
        f.getContentPane().add(p.add(window));

        f.setSize(camera.image_width + 16, (int)(camera.image_width / camera.aspect_ratio) + 39);
        f.setLocation(100, 100);

        f.addWindowListener(new WindowAdapter( ) {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        f.setVisible(true);
        Timer timer = new Timer(1000 / 24, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.render(world, window);
            }
        });
        timer.start();
    }

}