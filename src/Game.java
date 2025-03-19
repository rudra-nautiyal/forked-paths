import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Game {
    JFrame window;
    Container con;
    JPanel titleNamePanel, startButtonPanel, choiceButtonPanel;
    FadingPanel splashScreenPanel, mainTextPanel;
    JLabel titleNameLabel;
    JButton startButton, choice1, choice2, choice3, choice4;
    JTextArea mainTextArea, splashTextArea;
    Font titleFont, normalFont;

    TitleScreenHandler tsHandler = new TitleScreenHandler();
    SplashScreenHandler spHandler = new SplashScreenHandler();

    // Constants for fade timing
    private static final int FADE_DELAY = 50;  // milliseconds between fade steps
    private static final float FADE_STEP = 0.05f;  // amount to fade per step

    public static void main(String[] args) {
        new Game();
    }

    public Game() {
        try {
            File fontFile = new File("src/res/IMFellEnglishSC-Regular.ttf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            titleFont = customFont.deriveFont(Font.PLAIN, 70);
            normalFont = customFont.deriveFont(Font.PLAIN, 30);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            titleFont = new Font("Times New Roman", Font.PLAIN, 80);
            normalFont = new Font("Times New Roman", Font.PLAIN, 30);
        }

        window = new JFrame();
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.black);
        window.setLayout(null);
        window.setVisible(true);
        con = window.getContentPane();

        createTitleScreen();
    }

    public void createTitleScreen() {
        titleNamePanel = new JPanel();
        titleNamePanel.setBounds(100, 100, 600, 150);
        titleNamePanel.setBackground(Color.black);

        titleNameLabel = new JLabel("FORKED PATHS") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(Color.gray);
                g2.drawString(getText(), 5, 75);
                g2.setColor(Color.white);
                g2.drawString(getText(), 0, 70);
            }
        };
        titleNameLabel.setForeground(Color.white);
        titleNameLabel.setFont(titleFont);
        titleNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleNameLabel.setVerticalAlignment(SwingConstants.CENTER);

        startButtonPanel = new JPanel();
        startButtonPanel.setBounds(285, 350, 200, 100);
        startButtonPanel.setBackground(Color.black);

        startButton = createStartButton("START");

        startButton.setPreferredSize(new Dimension(220, 80));
        startButton.setFont(normalFont.deriveFont(Font.BOLD, 35));
        startButton.setBackground(Color.black);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createLineBorder(Color.white, 3));
        startButton.addActionListener(spHandler);

        titleNamePanel.add(titleNameLabel);
        startButtonPanel.add(startButton);

        // Create FadingPanels instead of regular JPanels
        FadingPanel fadingTitlePanel = new FadingPanel();
        fadingTitlePanel.setBounds(titleNamePanel.getBounds());
        fadingTitlePanel.setBackground(Color.black);
        fadingTitlePanel.add(titleNameLabel);

        FadingPanel fadingStartPanel = new FadingPanel();
        fadingStartPanel.setBounds(startButtonPanel.getBounds());
        fadingStartPanel.setBackground(Color.black);
        fadingStartPanel.add(startButton);

        con.add(fadingTitlePanel);
        con.add(fadingStartPanel);

        // Fade in the title screen
        fadeInComponent(fadingTitlePanel, null);
        fadeInComponent(fadingStartPanel, null);
    }

    public void fadeToSplashScreen() {
        // First find all FadingPanels in the container
        Component[] components = con.getComponents();
        final int componentCount = components.length;
        final int[] fadedCount = {0};

        for (Component comp : components) {
            if (comp instanceof FadingPanel) {
                final FadingPanel panel = (FadingPanel) comp;
                fadeOutComponent(panel, () -> {
                    con.remove(panel);
                    fadedCount[0]++;
                    if (fadedCount[0] >= componentCount) {
                        createSplashScreen();
                    }
                });
            } else {
                con.remove(comp);
                fadedCount[0]++;
            }
        }

        // If no components need fading, create splash screen directly
        if (fadedCount[0] >= componentCount) {
            createSplashScreen();
        }
    }

    public void createSplashScreen() {
        splashScreenPanel = new FadingPanel();
        splashScreenPanel.setBounds(100, 100, 600, 250);
        splashScreenPanel.setBackground(Color.black);
        splashScreenPanel.setOpacity(0f); // Start invisible
        con.add(splashScreenPanel);

        splashTextArea = new JTextArea("Every path you take carves your fate. Some choices lead forward, others to ruin. Remember, your choices are the reason the path before you forks.");
        splashTextArea.setBounds(0, 0, 600, 250);
        splashTextArea.setBackground(Color.black);
        splashTextArea.setForeground(Color.white);
        splashTextArea.setFont(normalFont);
        splashTextArea.setLineWrap(true);
        splashTextArea.setWrapStyleWord(true);
        splashTextArea.setEditable(false);
        splashScreenPanel.add(splashTextArea);

        con.revalidate();
        con.repaint();

        // Fade in the splash screen
        fadeInComponent(splashScreenPanel, () -> {
            // After fully faded in, wait a moment, then fade out
            Timer waitTimer = new Timer(2000, e -> {
                fadeOutComponent(splashScreenPanel, () -> {
                    con.remove(splashScreenPanel);
                    createGameScreen();
                });
            });
            waitTimer.setRepeats(false);
            waitTimer.start();
        });
    }

    public void createGameScreen() {
        mainTextPanel = new FadingPanel();
        mainTextPanel.setBounds(100, 100, 600, 250);
        mainTextPanel.setBackground(Color.black);
        mainTextPanel.setOpacity(0f); // Start invisible
        con.add(mainTextPanel);

        mainTextArea = new JTextArea("This is the main text area. Remember, your decisions are the reason your paths are forked...");
        mainTextArea.setBounds(0, 0, 600, 250);
        mainTextArea.setBackground(Color.black);
        mainTextArea.setForeground(Color.white);
        mainTextArea.setFont(normalFont);
        mainTextArea.setLineWrap(true);
        mainTextArea.setWrapStyleWord(true);
        mainTextArea.setEditable(false);
        mainTextPanel.add(mainTextArea);

        choiceButtonPanel = new JPanel();
        choiceButtonPanel.setBounds(250, 350, 300, 150);
        choiceButtonPanel.setBackground(Color.black);
        choiceButtonPanel.setLayout(new GridLayout(4, 1));

        // Create a fading panel for the choice buttons
        FadingPanel fadingChoicePanel = new FadingPanel();
        fadingChoicePanel.setBounds(choiceButtonPanel.getBounds());
        fadingChoicePanel.setBackground(Color.black);
        fadingChoicePanel.setLayout(new GridLayout(4, 1));
        fadingChoicePanel.setOpacity(0f); // Start invisible

        choice1 = createChoiceButton("This is choice one");
        choice2 = createChoiceButton("This is choice two");
        choice3 = createChoiceButton("This is choice three");
        choice4 = createChoiceButton("This is choice four");

        fadingChoicePanel.add(choice1);
        fadingChoicePanel.add(choice2);
        fadingChoicePanel.add(choice3);
        fadingChoicePanel.add(choice4);

        con.add(fadingChoicePanel);
        con.revalidate();
        con.repaint();

        // Fade in the game elements
        fadeInComponent(mainTextPanel, null);
        fadeInComponent(fadingChoicePanel, null);
    }

    // Fade in a component with optional callback when complete
    private void fadeInComponent(FadingPanel panel, Runnable onComplete) {
        Timer fadeInTimer = new Timer(FADE_DELAY, new ActionListener() {
            float opacity = 0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity = Math.min(opacity + FADE_STEP, 1.0f);
                panel.setOpacity(opacity);

                if (opacity >= 1.0f) {
                    ((Timer) e.getSource()).stop();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
            }
        });
        fadeInTimer.start();
    }

    // Fade out a component with optional callback when complete
    private void fadeOutComponent(FadingPanel panel, Runnable onComplete) {
        Timer fadeOutTimer = new Timer(FADE_DELAY, new ActionListener() {
            float opacity = panel.getOpacity();

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity = Math.max(0f, opacity - FADE_STEP);
                panel.setOpacity(opacity);

                if (opacity <= 0) {
                    ((Timer) e.getSource()).stop();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
            }
        });
        fadeOutTimer.start();
    }

    public class SplashScreenHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            fadeToSplashScreen();
        }
    }

    public class TitleScreenHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            createGameScreen();
        }
    }

    public JButton createChoiceButton(String text) {
        JButton button = new JButton(text) {
            private Color textColor = Color.white;
            private Color borderColor = new Color(120, 120, 120);
            private Color hoverBorderColor = new Color(255, 215, 0); // Gold color
            private Color hoverTextColor = new Color(255, 255, 200); // Light yellow
            private Color pressedColor = new Color(50, 50, 50);

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Set the background color
                if (getModel().isPressed()) {
                    g2d.setColor(pressedColor);
                } else {
                    g2d.setColor(Color.black);
                }
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Set the text color based on hover state
                if (getModel().isRollover()) {
                    textColor = hoverTextColor;
                    borderColor = hoverBorderColor;
                } else {
                    textColor = Color.white;
                    borderColor = new Color(120, 120, 120);
                }

                // Draw beveled edges for 3D effect
                g2d.setColor(new Color(70, 70, 70)); // Dark edge
                g2d.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1); // Bottom
                g2d.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1); // Right

                g2d.setColor(new Color(130, 130, 130)); // Light edge
                g2d.drawLine(0, 0, getWidth() - 1, 0); // Top
                g2d.drawLine(0, 0, 0, getHeight() - 1); // Left

                // Draw glowing border
                int thickness = 2;
                g2d.setColor(borderColor);
                g2d.drawRect(thickness, thickness, getWidth() - (2 * thickness) - 1, getHeight() - (2 * thickness) - 1);

                // Create a subtle gradient effect for the button background
                if (getModel().isRollover()) {
                    GradientPaint gp = new GradientPaint(
                            0, 0, new Color(40, 40, 40),
                            0, getHeight(), new Color(20, 20, 20));
                    g2d.setPaint(gp);
                    g2d.fillRect(thickness + 1, thickness + 1, getWidth() - (2 * thickness) - 2, getHeight() - (2 * thickness) - 2);
                }

                // Draw the text with a shadow effect
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                int textX = (getWidth() - textWidth) / 2;
                int textY = (getHeight() - textHeight) / 2 + fm.getAscent();

                // Draw text shadow
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.drawString(getText(), textX + 2, textY + 2);

                // Draw actual text
                g2d.setColor(textColor);
                g2d.drawString(getText(), textX, textY);
            }
        };

        // Configure the button
        button.setFont(normalFont);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return button;
    }

    public JButton createStartButton(String text) {
        JButton button = new JButton(text) {
            private Color textColor = Color.white;
            private Color borderColor = new Color(180, 180, 180);
            private Color hoverBorderColor = new Color(255, 215, 0); // Gold color
            private Color hoverTextColor = new Color(255, 255, 200); // Light yellow

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Set the background color
                g2d.setColor(Color.black);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Set the text and border color based on hover state
                if (getModel().isRollover()) {
                    textColor = hoverTextColor;
                    borderColor = hoverBorderColor;
                } else {
                    textColor = Color.white;
                    borderColor = new Color(180, 180, 180);
                }

                // Draw outer border
                int thickness = 3;
                g2d.setColor(borderColor);

                // Create a glowing effect for the border
                if (getModel().isRollover()) {
                    // Create multiple borders with decreasing opacity for glow effect
                    for (int i = 0; i < 3; i++) {
                        float alpha = 0.3f - (i * 0.1f);
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                        g2d.drawRect(thickness - i - 1, thickness - i - 1,
                                getWidth() - (thickness - i - 1) * 2,
                                getHeight() - (thickness - i - 1) * 2);
                    }
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }

                g2d.drawRect(thickness, thickness, getWidth() - (2 * thickness), getHeight() - (2 * thickness));

                // Create a subtle gradient for the button
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(30, 30, 30),
                        0, getHeight(), new Color(10, 10, 10));
                g2d.setPaint(gp);
                g2d.fillRect(thickness + 1, thickness + 1,
                        getWidth() - (2 * thickness) - 2,
                        getHeight() - (2 * thickness) - 2);

                // Draw the text with a shadow effect
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                int textX = (getWidth() - textWidth) / 2;
                int textY = (getHeight() - textHeight) / 2 + fm.getAscent();

                // Draw text shadow
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.drawString(getText(), textX + 2, textY + 2);

                // Draw actual text
                g2d.setColor(textColor);
                g2d.drawString(getText(), textX, textY);

                // Add a highlight effect at top
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.drawLine(thickness + 5, thickness + 5, getWidth() - thickness - 5, thickness + 5);
            }
        };

        // Configure the button
        button.setPreferredSize(new Dimension(220, 80));
        button.setFont(normalFont.deriveFont(Font.BOLD, 35));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return button;
    }

    class FadingPanel extends JPanel {
        private float opacity = 1.0f;

        public FadingPanel() {
            setOpaque(false);
        }

        public float getOpacity() {
            return opacity;
        }

        public void setOpacity(float opacity) {
            this.opacity = Math.max(0f, Math.min(1f, opacity));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            super.paint(g2);
            g2.dispose();
        }
    }
}