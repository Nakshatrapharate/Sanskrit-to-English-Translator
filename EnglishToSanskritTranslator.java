import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Hashtable;

public class EnglishToSanskritTranslator {
    public static void main(String[] args) {
        // Create the main application window
        JFrame frame = new JFrame("Advanced Sanskrit Translator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        // Initialize a hashtable to store English-to-Sanskrit translations
        Hashtable<String, String> mot = new Hashtable<>();
        mot.put("HI", "नमस्कार");
        mot.put("MY", "मम");
        mot.put("NAME", "नामः");
        mot.put("IS", "अस्ति");
        mot.put("BYE", "पुनर्मिलामः");
        mot.put("THANKS", "धन्यवादा");
        mot.put("WELCOME", "स्वागतम्‌");
        mot.put("HELLO", "नमस्ते");
        mot.put("HOW", "कथम्‌");
        mot.put("ARE", "सन्ति");
        mot.put("YOU", "त्वम्‌");
        mot.put(".", "|");
        mot.put("WHAT", "किम्‌");
        mot.put("DOING", "करोति");

        // Text area for user input
        JTextArea inputText = new JTextArea(10, 20);
        inputText.setFont(new Font("Arial", Font.PLAIN, 18));

        // Text area for output (translated text)
        JTextArea outputText = new JTextArea(10, 20);
        outputText.setFont(new Font("Arial", Font.PLAIN, 18));
        outputText.setEditable(false);
        
        // Panel for translation history
        JTextArea historyText = new JTextArea(8, 20);
        historyText.setFont(new Font("Arial", Font.PLAIN, 16));
        historyText.setEditable(false);
        
        // Add scroll panes to the text areas
        JScrollPane inputScrollPane = new JScrollPane(inputText);
        JScrollPane outputScrollPane = new JScrollPane(outputText);
        JScrollPane historyScrollPane = new JScrollPane(historyText);

        // Buttons for actions
        JButton translateButton = new JButton("Translate");
        JButton clearButton = new JButton("Clear");
        JButton fileUploadButton = new JButton("Upload File");
        JButton copyButton = new JButton("Copy Translation");

        // Language Selection Dropdown
        String[] languages = {"English to Sanskrit", "English to Hindi"};
        JComboBox<String> languageDropdown = new JComboBox<>(languages);

        // Set up output font with Devanagari (Mangal font)
        try {
            Font devanagariFont = Font.createFont(Font.TRUETYPE_FONT, new File("Mangal Regular.ttf")).deriveFont(18f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(devanagariFont);
            outputText.setFont(devanagariFont);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Translate button functionality
        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputText.getText();
                String[] words = input.split("\\s");
                StringBuilder translation = new StringBuilder();

                for (String word : words) {
                    if (mot.containsKey(word)) {
                        translation.append(mot.get(word)).append(" ");
                    } else {
                        translation.append(word).append(" ");
                    }
                }
                String translatedText = translation.toString();
                outputText.setText(translatedText);

                // Update history log
                historyText.append("Input: " + input + "\nTranslation: " + translatedText + "\n\n");
            }
        });

        // Clear button functionality
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputText.setText("");
                outputText.setText("");
            }
        });

        // File upload button functionality
        fileUploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        String fileContent = new String(Files.readAllBytes(file.toPath()));
                        inputText.setText(fileContent);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error reading file", "File Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Copy to clipboard functionality
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(outputText.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
                JOptionPane.showMessageDialog(frame, "Translation copied to clipboard!");
            }
        });

        // Organize panels
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(translateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(fileUploadButton);
        buttonPanel.add(copyButton);

        JPanel languagePanel = new JPanel(new FlowLayout());
        languagePanel.add(new JLabel("Select Language: "));
        languagePanel.add(languageDropdown);

        JPanel textPanel = new JPanel(new GridLayout(3, 1));
        textPanel.add(new JLabel("Input Text:"));
        textPanel.add(inputScrollPane);
        textPanel.add(new JLabel("Translation:"));
        textPanel.add(outputScrollPane);
        textPanel.add(new JLabel("Translation History:"));
        textPanel.add(historyScrollPane);

        // Add everything to the main frame
        frame.setLayout(new BorderLayout());
        frame.add(languagePanel, BorderLayout.NORTH);
        frame.add(textPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
