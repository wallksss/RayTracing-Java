import org.jocl.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.util.Random;

import static org.jocl.CL.*;

public class Renderer {
    private Camera camera;
    private Hittable world;
    private static int width;
    private static int height;

    public Renderer(Camera camera, Hittable world) {
        this.camera = camera;
        width = camera.image_width;
        height = camera.image_height;
        this.world = world;
    }

    public Camera getCamera() {
        return camera;
    }

    public static void render(BufferedImage image, HostManager hostManager, cl_mem outputImageMem, cl_mem cameraMem) {
        long[] globalWorkSize = new long[2];
        globalWorkSize[0] = width;
        globalWorkSize[1] = height;

        int[] seeds = new int[width * height];
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < seeds.length; i++) {
            seeds[i] = random.nextInt();
        }

        clSetKernelArg(hostManager.getKernel(), 0, Sizeof.cl_mem, Pointer.to(outputImageMem));
        clSetKernelArg(hostManager.getKernel(), 1, Sizeof.cl_uint, Pointer.to(new int[]{width}));
        clSetKernelArg(hostManager.getKernel(), 2, Sizeof.cl_uint, Pointer.to(new int[]{height}));
        CL.clSetKernelArg(hostManager.getKernel(), 3, Sizeof.cl_mem, Pointer.to(cameraMem));
        clEnqueueNDRangeKernel(hostManager.getCommandQueue(), hostManager.getKernel(), 2, null, globalWorkSize, null, 0, null, null);

        int[] output = new int[width * height];
        clEnqueueReadBuffer(hostManager.getCommandQueue(), outputImageMem, CL_TRUE, 0, width * height * Sizeof.cl_int, Pointer.to(output), 0, null, null);
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                image.setRGB(i, j, output[j * width + i]);
            }
        }
    }
}

