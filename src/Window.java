import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.ByteBuffer;
import java.util.List;

public class Window extends JComponent implements Runnable {
    private Camera camera;
    private ImagePanel imagePanel;
    private JFrame window;
    private OpenCLUtils.Device selectedDevice;

    public Window(String title, int width, int height, Camera camera) {
        this.camera = camera;

        selectDeviceWindow();

        HostManager.addDevice(selectedDevice);
        HostManager.createContext(selectedDevice);
        HostManager.createCommandQueue(selectedDevice);
        HostManager.createProgram("src/kernels/render.cl");
        HostManager.createKernel("render_kernel");
        HostManager hostManager = HostManager.getInstance();

        this.window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int cameraMemSize = camera.getCameraSize();
        ByteBuffer cameraMem = camera.toByteBuffer(cameraMemSize);
        this.imagePanel = new ImagePanel(width, height, hostManager, cameraMem, camera);
        window.add(imagePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public void selectDeviceWindow() {
        JDialog deviceWindow = new JDialog(window, "Selecione um Dispositivo OpenCL", true);
        deviceWindow.setSize(400, 300);
        deviceWindow.setLocationRelativeTo(window);

        deviceWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel devicePanel = new JPanel();
        devicePanel.setLayout(new BoxLayout(devicePanel, BoxLayout.Y_AXIS));

        ButtonGroup group = new ButtonGroup();

        List<OpenCLUtils.Platform> platforms = OpenCLUtils.Platform.getPlatforms();
        for (OpenCLUtils.Platform value : platforms) {
            JLabel platformName = new JLabel(value.getPlatformName());
            devicePanel.add(platformName);

            List<OpenCLUtils.Device> devices = OpenCLUtils.Device.getDevices(value.getPlatformId());
            for (OpenCLUtils.Device device : devices) {
                JRadioButton option = new JRadioButton(device.getDeviceName(), false);
                group.add(option);
                devicePanel.add(option);
            }
        }

        panel.add(devicePanel, BorderLayout.CENTER);

        JButton selectButton = new JButton("Selecionar Dispositivo");
        selectButton.addActionListener(e -> {
            for (Component comp : devicePanel.getComponents()) {
                if (comp instanceof JRadioButton) {
                    JRadioButton radioButton = (JRadioButton) comp;
                    if (radioButton.isSelected()) {
                        String selectedDeviceName = radioButton.getText();
                        for (OpenCLUtils.Platform platform : platforms) {
                            List<OpenCLUtils.Device> devices = OpenCLUtils.Device.getDevices(platform.getPlatformId());
                            for (OpenCLUtils.Device device : devices) {
                                if (device.getDeviceName().equals(selectedDeviceName)) {
                                    selectedDevice = device;
                                    JOptionPane.showMessageDialog(deviceWindow, "Dispositivo selecionado: " + selectedDevice.getDeviceName()
                                    + "\nTipo de dispositivo: " + selectedDevice.getDeviceType()
                                    + "\nUnidades computacionais: " + selectedDevice.getDeviceMaxComputeUnits()
                                    + "\nTamanho do Grupo de Trabalho: " + selectedDevice.getDeviceMaxGroupWorkSize()
                                    + "\nVersao do OpenCL: " + selectedDevice.getDeviceOpenCLVersion());
                                    deviceWindow.dispose();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(deviceWindow, "Por favor, selecione um dispositivo.");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        deviceWindow.add(panel);
        deviceWindow.setVisible(true);
    }

    public void run() {
        while (true) {
            imagePanel.repaint();
            try { //controle de fps (7 eh +- 120fps e 16 eh +- 60fps)
                Thread.sleep(7);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}