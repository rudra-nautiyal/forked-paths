import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class Game {

    JFrame window;
    Container con;
    JPanel titleNamePanel, startButtonPanel, splashScreenPanel ,mainTextPanel, choiceButtonPanel;
    JLabel titleNameLabel;
    JButton startButton, choice1, choice2, choice3, choice4;
    JTextArea mainTextArea, splashTextArea;
    Font titleFont, normalFont;

    TitleScreenHandler tsHandler = new TitleScreenHandler();
    SplashScreenHandler spHandler = new SplashScreenHandler();

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
        startButton.addActionListener(spHandler);

        titleNamePanel.add(titleNameLabel);
        startButtonPanel.add(startButton);

        con.add(titleNamePanel);
        con.add(startButtonPanel);
    }

    public void createSplashScreen() {
        titleNamePanel.setVisible(false);
        startButtonPanel.setVisible(false);

        splashScreenPanel = new JPanel();
        splashScreenPanel.setBounds(100, 100, 600, 250);
        splashScreenPanel.setBackground(Color.red);
        con.add(splashScreenPanel);

        splashTextArea = new JTextArea("Remember, your decisions are the reason your paths are forked...");
        splashTextArea.setBounds(100, 100, 600, 250);
        splashTextArea.setBackground(Color.black);
        splashTextArea.setForeground(Color.white);
        splashTextArea.setFont(normalFont);
        splashTextArea.setLineWrap(true);
        splashTextArea.setWrapStyleWord(true);
        splashTextArea.setEditable(false);
        splashScreenPanel.add(splashTextArea);

        // Timer to switch screens after 3 seconds
        Timer delay = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                con.remove(splashScreenPanel); // Remove splash screen
                createGameScreen(); // Show main game screen
                con.revalidate(); // Refresh UI
                con.repaint();
            }
        });

        delay.setRepeats(false); // Run only once
        delay.start();
    }

    public void createGameScreen() {

        titleNamePanel.setVisible(false);
        startButtonPanel.setVisible(false);

        mainTextPanel = new JPanel();
        mainTextPanel.setBounds(100, 100, 600, 250);
        mainTextPanel.setBackground(Color.black);
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

        choiceButtonPanel = new JPanel();
        choiceButtonPanel.setBounds(250, 350, 300, 150);
        choiceButtonPanel.setBackground(Color.black);
        choiceButtonPanel.setLayout(new GridLayout(4, 1));
        con.add(choiceButtonPanel);

        choice1 = ChoiceButton("This is choice one");
        choiceButtonPanel.add(choice1);
        choice2 = ChoiceButton("This is choice two");
        choiceButtonPanel.add(choice2);
        choice3 = ChoiceButton("This is choice three");
        choiceButtonPanel.add(choice3);
        choice4 = ChoiceButton("This is choice four");
        choiceButtonPanel.add(choice4);

    }

    public class SplashScreenHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            createSplashScreen();
        }
    }

    public class TitleScreenHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            createGameScreen();
        }
    }

    public JButton ChoiceButton(String text) {
        JButton button;
        button = new JButton();
        button.setBackground(Color.black);
        button.setForeground(Color.white);
        button.setFont(normalFont);
        button.setText(text);
        button.setFocusPainted(false);
        return button;
    }
}
