public class Image {
    private int width;
    private int height;
    private double aspectRatio;
    Image(int width, int height) {
        this.width = width;
        this.height = height;
    }

    Image(int width, double aspectRatio) {
        this.width = width;
        this.height = (int)(width/aspectRatio);
        this.height = Math.max(this.height, 1); //ensure that height is at least 1
        this.aspectRatio = aspectRatio;
    }

    int getWidth(){
        return  width;
    }
    int getHeight(){
        return  height;
    }
    double getAspectRatio() {return aspectRatio;}
}