import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private final int width;
    private final int height;
    private final Renderer renderer;
    private final BufferedImage image;

    public ImagePanel(int width, int height, Renderer renderer) {
        this.width = width;
        this.height = height;
        this.renderer = renderer;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        setupKeyBindings();
    }

    private void setupKeyBindings() {
        this.getInputMap().put(KeyStroke.getKeyStroke("W"), "moveForward");
        this.getActionMap().put("moveForward", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraCenter(0, 0, 1);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("S"), "moveBackward");
        this.getActionMap().put("moveBackward", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraCenter(0, 0, -1);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("A"), "moveLeft");
        this.getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraCenter(1, 0, 0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("D"), "moveRight");
        this.getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraCenter(-1, 0, 0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "moveUp");
        this.getActionMap().put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraCenter(0, 1, 0);
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ctrl pressed CONTROL"), "moveDown");
        this.getActionMap().put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraCenter(0, -1, 0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "moveCameraLeft");
        this.getActionMap().put("moveCameraLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().rotateCamera(0, 10);
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

                double sensitivity = 0.5;

                renderer.getCamera().rotateCamera(deltaY * sensitivity * -1, deltaX * sensitivity * -1);

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
        renderer.render(image);
        g.drawImage(image, 0, 0, this);
    }
}