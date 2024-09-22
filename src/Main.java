public class Main {
    public static void main(String[] args) {
        Image img = new Image(400, 16.0/9.0);
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        HittableList world = new HittableList();
        world.add(new Sphere(new Vec3(0, 0, -1), 0.5));
        world.add(new Sphere(new Vec3(0, 0, -1), 0.5));
        world.add(new Sphere(new Vec3(0, -100.5, -1), 100));


        Camera camera = new Camera(1.0, 2.0, (double)imgWidth/imgHeight * 2.0, new Vec3(0, 0, 0));
        Vec3 cameraCenter = camera.getCameraCenter();
        Vec3 focalLength = new Vec3(0, 0, camera.getFocalLength());

        Vec3 viewportU = new Vec3(camera.getViewportWidth(), 0, 0);
        Vec3 viewportV = new Vec3(0, -camera.getViewportHeight(), 0);

        Vec3 pixelDeltaU = viewportU.divide(imgWidth);
        Vec3 pixelDeltaV = viewportV.divide(imgHeight);

        Vec3 viewportUpperLeft = cameraCenter
                .subtract(focalLength)
                .subtract(viewportU.divide(2))
                .subtract(viewportV.divide(2));

        Vec3 pixel00 = viewportUpperLeft
                .add(pixelDeltaU.add(pixelDeltaV).multiply(0.5));

        Render render = new Render();
        render.renderImage(imgWidth, imgHeight, pixel00, pixelDeltaU, pixelDeltaV, cameraCenter, world);
    }
}