import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.*;
import java.awt.image.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javax.swing.*;

public class Window extends JComponent implements Runnable {
    byte[] data;
    static BufferedImage image;
    Random random;
    static int w;
    static int h;

    public void initialize() {
        w = getSize().width;
        h = getSize( ).height;
        data = new byte[w * h * 3]; // 3 bytes por pixel para RGB
        DataBuffer db = new DataBufferByte(data, data.length);
        WritableRaster wr = Raster.createInterleavedRaster(db, w, h, 3 * w, 3, new int[]{0, 1, 2}, null);
        ColorModel cm = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[]{8, 8, 8}, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        image = new BufferedImage(cm, wr, false, null);
        random = new Random();
    }

    public void setPixel(int x, int y, Color color) {
        if (x < 0 || x >= w || y < 0 || y >= h) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds: " + x + ", " + y);
        }
        int rgb = color.getRGB();

        image.setRGB(x, y, rgb);
    }

    public void run( ) {
        while (true) {
            random.nextBytes(data);
            repaint( );
            try { Thread.sleep(1000 / 24); }
            catch( InterruptedException e ) { /* die */ }
        }
    }

    public void paint(Graphics g) {
        if (image == null) initialize( );
        g.drawImage(image, 0, 0, this);
    }
}