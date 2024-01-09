import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ScreenCaptureApp {
    private final JLabel imageLabel;
    private Robot robot;
    private Point mouseDownCompCoordinate = null;

    public ScreenCaptureApp() {
        try {
            GraphicsConfiguration config = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();
            robot = new Robot(config.getDevice());
        } catch (AWTException e) {
            e.printStackTrace();
            System.exit(1);
        }

        JFrame frame = new JFrame("Image Capture App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1299, 954);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.setUndecorated(true);
        frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    mouseDownCompCoordinate = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoordinate = null;
            }
        });

        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseDownCompCoordinate != null && SwingUtilities.isRightMouseButton(e)) {
                    Point currCoords = e.getLocationOnScreen();
                    frame.setLocation(currCoords.x - mouseDownCompCoordinate.x, currCoords.y - mouseDownCompCoordinate.y);
                }
            }
        });

        imageLabel = new JLabel();
        frame.add(imageLabel);

        Timer timer = new Timer(10, e -> captureAndDisplayImage());

        frame.setVisible(true);
        timer.start();
    }

    private void captureAndDisplayImage() {
        Rectangle captureRect = new Rectangle(269, 0, 1299, 952);
        BufferedImage capture = robot.createScreenCapture(captureRect);

        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();

        Graphics2D g = capture.createGraphics();
        g.setColor(Color.RED);
        g.drawLine(mouseLocation.x - captureRect.x - 20, mouseLocation.y - captureRect.y,
                mouseLocation.x - captureRect.x + 20, mouseLocation.y - captureRect.y);
        g.drawLine(mouseLocation.x - captureRect.x, mouseLocation.y - captureRect.y - 20,
                mouseLocation.x - captureRect.x, mouseLocation.y - captureRect.y + 20);
        g.dispose();

        ImageIcon icon = new ImageIcon(capture);
        imageLabel.setIcon(icon);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ScreenCaptureApp::new);
    }
}
