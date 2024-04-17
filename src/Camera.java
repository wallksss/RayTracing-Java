public class Camera {
    private double focalLength;
    private double viewportHeight;
    private double viewportWidth;
    private Vec3 cameraCenter;
    Camera(double focalLength, double viewportHeight, double viewportWidth, Vec3 cameraCenter) {
        this.focalLength = focalLength;
        this.viewportHeight = viewportHeight;
        this.viewportWidth = viewportWidth;
        this.cameraCenter = cameraCenter;
    }

    double getViewportHeight() {return viewportHeight;}
    double getViewportWidth() {return viewportWidth;}
    double getFocalLength() {return focalLength;}
    Vec3 getCameraCenter() {return cameraCenter;}

}
