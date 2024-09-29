import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
                renderer.getCamera().setCameraCenter(0, 0, -1);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("S"), "moveBackward");
        this.getActionMap().put("moveBackward", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraCenter(0, 0, 1);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("A"), "moveLeft");
        this.getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraCenter(-1, 0, 0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("D"), "moveRight");
        this.getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraCenter(1, 0, 0);
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
                .put(KeyStroke.getKeyStroke("ctrl pressed CONTROL"), "controlPressed");
        this.getActionMap().put("controlPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraCenter(0, -1, 0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "moveCameraLeft");
        this.getActionMap().put("moveCameraLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraDirection(-1, 0, 0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "moveCameraRight");
        this.getActionMap().put("moveCameraRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraDirection(1, 0, 0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "moveCameraUp");
        this.getActionMap().put("moveCameraUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraDirection(0, 1, 0);
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "moveCameraDown");
        this.getActionMap().put("moveCameraDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.getCamera().setCameraDirection(0, -1, 0);
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