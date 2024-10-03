import org.jocl.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;

import static org.jocl.CL.*;

public class ImagePanel extends JPanel {
    private final int width;
    private final int height;
    private final BufferedImage image;
    private ByteBuffer cameraBuffer;
    private cl_mem cameraMem;
    private cl_mem outputImageMem;
    private HostManager hostManager;
    private Camera camera;

    public ImagePanel(int width, int height, HostManager hostManager, ByteBuffer cameraBuffer, Camera camera) {
        this.width = width;
        this.height = height;
        this.hostManager = hostManager;
        this.cameraBuffer = cameraBuffer;
        this.camera = camera;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        initImageMem();
        initCameraMem();
        setupKeyBindings();
    }

    private void initImageMem() {
        outputImageMem = clCreateBuffer(hostManager.getContext(), CL_MEM_WRITE_ONLY, width * height * Sizeof.cl_uint, null, null);
    }

    private void initCameraMem() {
        cameraMem = clCreateBuffer(hostManager.getContext(), CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, cameraBuffer.capacity(), Pointer.to(cameraBuffer), null);
    }

    private void updateCameraMem() {
        int cameraBufferSize = camera.getCameraSize();
        ByteBuffer newCameraBuffer = camera.toByteBuffer(cameraBufferSize);
        clEnqueueWriteBuffer(hostManager.getCommandQueue(), cameraMem, CL_TRUE, 0, newCameraBuffer.capacity(), Pointer.to(newCameraBuffer), 0, null, null);
    }

    private void setupKeyBindings() {
        this.getInputMap().put(KeyStroke.getKeyStroke("W"), "moveForward");
        this.getActionMap().put("moveForward", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.setCameraCenter(0, 0, -1);
                updateCameraMem();
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("S"), "moveBackward");
        this.getActionMap().put("moveBackward", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.setCameraCenter(0, 0, 1);
                updateCameraMem();
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("A"), "moveLeft");
        this.getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.setCameraCenter(-1, 0, 0);
                updateCameraMem();
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("D"), "moveRight");
        this.getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.setCameraCenter(1, 0, 0);
                updateCameraMem();
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "moveUp");
        this.getActionMap().put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.setCameraCenter(0, -1, 0);
                updateCameraMem();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ctrl pressed CONTROL"), "moveDown");
        this.getActionMap().put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.setCameraCenter(0, 1, 0);
                updateCameraMem();
            }
        });
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = toolkit.getImage(getClass().getResource("/cursor.png"));
        Cursor crosshairCursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "crosshair");
        setCursor(crosshairCursor);
        this.addMouseMotionListener(new MouseAdapter() {
            private int lastX = -1;
            private int lastY = -1;

            @Override
            public void mouseMoved(MouseEvent e) {
                if (lastX == -1 && lastY == -1) {
                    lastX = e.getX();
                    lastY = e.getY();
                    return;
                }

                int deltaX = e.getX() - lastX;
                int deltaY = e.getY() - lastY;

                float sensitivity = 0.5f;

                camera.rotateCamera(deltaY * sensitivity * -1, deltaX * sensitivity * -1);
                updateCameraMem();

                lastX = width / 2;
                lastY = height / 2;

                Robot robot;
                try {
                    robot = new Robot();
                    robot.mouseMove(getLocationOnScreen().x + width / 2,
                            getLocationOnScreen().y + height / 2);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lastX = -1;
                lastY = -1;
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Renderer.render(image, hostManager, outputImageMem, cameraMem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        g.drawImage(image, 0, 0, this);
    }
}