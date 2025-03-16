import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class Game {

    JFrame window;
    Container con;
    JPanel titleNamePanel, startButtonPanel, mainTextPanel;
    JLabel titleNameLabel;
    JButton startButton;
    JTextArea mainTextArea;
    Font titleFont, normalFont;

    TitleScreenHandler tsHandler = new TitleScreenHandler();

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

        titleNamePanel = new JPanel();
        titleNamePanel.setBounds(100, 100, 600, 150);
        titleNamePanel.setBackground(Color.black);
        titleNameLabel = new JLabel("FORKED PATHS") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);  // Call default drawing behavior
                Graphics2D g2 = (Graphics2D) g;  // Cast to Graphics2D for advanced effects
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  // Smoother text rendering

                // Shadow effect
                g2.setColor(Color.gray);  // Shadow color
                g2.drawString(getText(), 5, 75);  // Draw the text slightly offset (x+5, y+5)

                // Original text
                g2.setColor(Color.white);
                g2.drawString(getText(), 0, 70);  // Draw the text in the normal position
            }
        };
        titleNameLabel.setForeground(Color.white);
        titleNameLabel.setFont(titleFont);
        titleNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleNameLabel.setVerticalAlignment(SwingConstants.CENTER);


        startButtonPanel = new JPanel();
        startButtonPanel.setBounds(285, 350, 200, 100);
        startButtonPanel.setBackground(Color.black);

        startButton = new JButton("START") {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) { // Hover effect
                    setForeground(Color.yellow);
                    setBorder(BorderFactory.createLineBorder(Color.yellow, 3)); // Change border on hover
                } else {
                    setForeground(Color.white);
                    setBorder(BorderFactory.createLineBorder(Color.white, 3));
                }
                super.paintComponent(g);
            }
        };

        startButton.setPreferredSize(new Dimension(220, 80)); // Make the button bigger
        startButton.setFont(normalFont.deriveFont(Font.BOLD, 35)); // Increase font size
        startButton.setBackground(Color.black);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createLineBorder(Color.white, 3)); // White border
        startButton.addActionListener(tsHandler);

        titleNamePanel.add(titleNameLabel);
        startButtonPanel.add(startButton);

        con.add(titleNamePanel);
        con.add(startButtonPanel);
    }

    public void createGameScreen() {

        titleNamePanel.setVisible(false);
        startButtonPanel.setVisible(false);

        mainTextPanel = new JPanel();
        mainTextPanel.setBounds(100, 100, 600, 250);
        mainTextPanel.setBackground(Color.blue);
        con.add(mainTextPanel);

        mainTextArea = new JTextArea("This is the main text area. Remember, your decisions are the reason your paths are forked...");
        mainTextArea.setBounds(100, 100, 600, 250);
        mainTextArea.setBackground(Color.black);
        mainTextArea.setForeground(Color.white);
        mainTextArea.setFont(normalFont);
        mainTextArea.setLineWrap(true);
        mainTextArea.setWrapStyleWord(true);
        mainTextArea.setEditable(false);
        mainTextPanel.add(mainTextArea);
    }

    public class TitleScreenHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            createGameScreen();
        }
    }
}
