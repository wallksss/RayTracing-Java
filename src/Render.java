public class Render {
    void renderImage(int imgWidth, int imgHeight, Vec3 pixel00, Vec3 pixelDeltaU, Vec3 pixelDeltaV, Vec3 cameraCenter) {
        System.out.println("P3");
        System.out.println(imgWidth + " " + imgHeight);
        System.out.println(("255"));

        for(int j = 0; j < imgHeight; j++) {
            System.err.print("\rScanlines remaining: " + (imgHeight - j) + " ");
            System.err.flush();
            for(int i = 0; i < imgWidth; i++) {
                Vec3 pixelCenter = pixel00
                        .add(pixelDeltaU.multiply(i))
                        .add(pixelDeltaV.multiply(j));
                Vec3 rayDirection = pixelCenter.subtract(cameraCenter);

                Ray r = new Ray(cameraCenter, rayDirection);

                Vec3 pixelColor = Ray.rayColor(r);
                Util.writeColor(pixelColor);
            }
        }
        System.err.print("Done\n");
    }
}