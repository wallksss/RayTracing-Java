import javax.swing.*;

public class Window extends JComponent implements Runnable {
    private Renderer renderer;
    private ImagePanel imagePanel;
    private JFrame window;

    public Window(String title, int width, int height, Renderer renderer) {
        this.renderer = renderer;

        this.window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.imagePanel = new ImagePanel(width, height, renderer);
        window.add(imagePanel);
        window.pack();
        window.setVisible(true);
    }

    public void run() {
        while (true) {
            imagePanel.repaint();
            try {
                Thread.sleep(1000 / 24);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}