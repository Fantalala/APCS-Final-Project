package PRISMS.AP_CS.Final_Project.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Drafter extends JFrame {
    private List<JLabel> pickRowLabels;
    private List<JLabel> banRowLabels;
    private int nextPickRowIndex = 0;
    private int nextBanRowIndex = 0;
    private List<String> championNames;
    private int draftOrder = 0;
    private String[] BPOrder = {
        "ban", "ban", "ban", "ban", "ban", "ban",
        "pick", "pick", "pick", "pick", "pick", "pick",
        "ban", "ban", "ban", "ban", "pick", "pick", "pick", "pick"
    };
    private String[] teamOrder = {
        "blue", "red", "blue", "red", "blue", "red",
        "blue", "red", "red", "blue", "blue", "red",
        "blue", "red", "blue", "red", "red", "blue",
        "blue", "red"
    };
    private static final int ICON_SIZE = 100;
    private JLabel statusLabel;
    private String[] blueTeam = new String[5];
    private String[] redTeam = new String[5];

    public Drafter() {
        setTitle("League of Legends Drafter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        initializeChampionNames();
        initializeUI();
    }

    private void initializeChampionNames() {
        championNames = new ArrayList<>();
        championNames.add("Aatrox");
        championNames.add("Ahri");
        championNames.add("Akali");
        championNames.add("Akshan");
        championNames.add("Alistar");
        championNames.add("Amumu");
        championNames.add("Anivia");
        championNames.add("Annie");
        championNames.add("Aphelios");
        championNames.add("Ashe");
        championNames.add("Aurelion_Sol");
        championNames.add("Azir");
        championNames.add("Bard");
        championNames.add("Blitzcrank");
        championNames.add("Brand");
        championNames.add("Braum");
        championNames.add("Caitlyn");
        championNames.add("Camille");
        championNames.add("Cassiopeia");
        championNames.add("Cho'Gath");

        // Add more champion names as needed
    }

    private void initializeUI() {
        JPanel iconPoolPanel = new JPanel();
        iconPoolPanel.setLayout(new GridLayout(0, 11, 10, 10)); 

        initializeBanRow();
        initializePickRow();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        statusLabel = new JLabel();
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        updateStatusLabel();
        topPanel.add(statusLabel, BorderLayout.NORTH);

        JPanel rowsPanel = new JPanel();
        rowsPanel.setLayout(new GridLayout(0, 1, 10, 10)); 

        rowsPanel.add(createBanRowPanel());
        rowsPanel.add(createPickRowPanel());

        topPanel.add(rowsPanel, BorderLayout.CENTER);

        loadChampionImages(iconPoolPanel);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        getContentPane().add(new JScrollPane(iconPoolPanel), BorderLayout.CENTER);
    }

    private void initializeBanRow() {
        banRowLabels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            JLabel blankLabel = new JLabel();
            blankLabel.setHorizontalAlignment(JLabel.CENTER);
            blankLabel.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
            banRowLabels.add(blankLabel);
        }
    }

    private JPanel createBanRowPanel() {
        JPanel banRowPanel = new JPanel();
        banRowPanel.setLayout(new GridLayout(1, 11, 10, 10));

        for (int i = 0; i < 5; i++) {
            banRowPanel.add(banRowLabels.get(i));
        }

        banRowPanel.add(new JSeparator(SwingConstants.VERTICAL));

        for (int i = 5; i < 10; i++) {
            banRowPanel.add(banRowLabels.get(i));
        }

        return banRowPanel;
    }

    private void initializePickRow() {
        pickRowLabels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            JLabel blankLabel = new JLabel();
            blankLabel.setHorizontalAlignment(JLabel.CENTER);
            blankLabel.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
            pickRowLabels.add(blankLabel);
        }
    }

    private JPanel createPickRowPanel() {
        JPanel pickRowPanel = new JPanel();
        pickRowPanel.setLayout(new GridLayout(1, 11, 10, 10));

        for (int i = 0; i < 5; i++) {
            pickRowPanel.add(pickRowLabels.get(i));
        }

        pickRowPanel.add(new JSeparator(SwingConstants.VERTICAL));

        for (int i = 5; i < 10; i++) {
            pickRowPanel.add(pickRowLabels.get(i));
        }

        return pickRowPanel;
    }

    private void loadChampionImages(JPanel panel) {
        for (String name : championNames) {
            URL imageUrl = getClass().getClassLoader().getResource("PRISMS/AP_CS/Final_Project/images/" + name + ".jpg");
            if (imageUrl != null) {
                ImageIcon imageIcon = resizeIcon(new ImageIcon(imageUrl), ICON_SIZE, ICON_SIZE);
                JLabel imageLabel = new JLabel(imageIcon);
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
                imageLabel.setName(name);

                JLabel nameLabel = new JLabel(name);
                nameLabel.setHorizontalAlignment(JLabel.CENTER);

                JPanel imagePanel = new JPanel();
                imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                imagePanel.add(imageLabel);
                imagePanel.add(nameLabel);

                imageLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (imageLabel.isEnabled()) {
                            if (BPOrder[draftOrder].equals("ban")) {
                                ImageIcon grayscaleIcon = toGrayscaleIcon(imageIcon);
                                if (teamOrder[draftOrder].equals("blue")) {
                                    placeIcon(banRowLabels, grayscaleIcon, true);
                                } else {
                                    placeIcon(banRowLabels, grayscaleIcon, false);
                                }
                            } else if (BPOrder[draftOrder].equals("pick")) {
                                if (teamOrder[draftOrder].equals("blue")) {
                                    blueTeam[nextPickRowIndex] = name;
                                    placeIcon(pickRowLabels, imageIcon, true);
                                } else {
                                    redTeam[nextPickRowIndex] = name;
                                    placeIcon(pickRowLabels, imageIcon, false);
                                }
                            }
                            imageLabel.setEnabled(false);
                            draftOrder++;
                            updateStatusLabel();
                        }
                    }
                });

                panel.add(imagePanel);
            } else {
                JLabel label = new JLabel(name + " image not found.");
                label.setHorizontalAlignment(JLabel.CENTER);
                panel.add(label);
            }
        }
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private ImageIcon toGrayscaleIcon(ImageIcon icon) {
        Image img = icon.getImage();
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                int rgb = bufferedImage.getRGB(i, j);
                int alpha = (rgb >> 24) & 0xff;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;
                int gray = (red + green + blue) / 3;
                int newRgb = (alpha << 24) | (gray << 16) | (gray << 8) | gray;
                bufferedImage.setRGB(i, j, newRgb);
            }
        }

        return new ImageIcon(bufferedImage.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    }

    private void placeIcon(List<JLabel> rowLabels, ImageIcon icon, boolean isBlueTeam) {
        if (isBlueTeam) {
            for (int i = 0; i < rowLabels.size(); i++) {
                if (rowLabels.get(i).getIcon() == null) {
                    rowLabels.get(i).setIcon(icon);
                    break;
                }
            }
        } else {
            for (int i = rowLabels.size() - 1; i >= 0; i--) {
                if (rowLabels.get(i).getIcon() == null) {
                    rowLabels.get(i).setIcon(icon);
                    break;
                }
            }
        }
    }

    private int evaluateScore(String[] team) {
        int score = 0;
        for (String champion : team) {
            if (champion != null) {
                // Calculate the score based on the champion
            }
        }
        return score;
    }

    private void updateStatusLabel() {
        if (draftOrder < BPOrder.length) {
            statusLabel.setText(teamOrder[draftOrder] + " " + BPOrder[draftOrder]);
        } else {
            TeamEvaluator evaluator = new TeamEvaluator();
            int blueScore = evaluator.evaluateTeam(blueTeam);
            int redScore = evaluator.evaluateTeam(redTeam);
            statusLabel.setText("Drafting complete. Blue team score: " + blueScore + "Red team score: " + redScore);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Drafter drafter = new Drafter();
            drafter.setVisible(true);
        });
    }
}
