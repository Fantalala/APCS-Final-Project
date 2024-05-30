package PRISMS.AP_CS.Final_Project.src;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImageTest extends JFrame {
    public ImageTest() {
        setTitle("Image Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JLabel imageLabel = new JLabel();

        try {
            // Load image from the path
            URL imageUrl = getClass().getClassLoader().getResource("PRISMS/AP_CS/Final_Project/images/Ahri.jpg");
            if (imageUrl != null) {
                ImageIcon imageIcon = resizeIcon(new ImageIcon(imageUrl), 160, 160);
                imageLabel.setIcon(imageIcon);
            } else {
                imageLabel.setText("Image not found");
            }
        } catch (Exception e) {
            imageLabel.setText("Error loading image");
            System.err.println("Error loading image: " + e.getMessage());
        }

        panel.add(imageLabel);
        add(panel);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageTest frame = new ImageTest();
            frame.setVisible(true);
        });
    }
}
