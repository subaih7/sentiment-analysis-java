import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class SentimentAnalysisGUI extends JFrame {
    private JTextArea outputArea;
    private JButton uploadButton;

    public SentimentAnalysisGUI() {
        setTitle("Sentiment Analysis Tool");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        uploadButton = new JButton("Start Sentiment Analysis");
        uploadButton.addActionListener(e -> {
            try {
                runAnalysis();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        add(uploadButton, BorderLayout.SOUTH);
    }

    private void runAnalysis() throws IOException {
        Set<String> positiveWords = SentimentAnalysisSequential.loadWords("positive_words.txt");
        Set<String> negativeWords = SentimentAnalysisSequential.loadWords("negative_words.txt");
        List<String> lines = SentimentAnalysisSequential.loadLines("data.txt");

        List<String> results = new ArrayList<>();
        for (String line : lines) {
            results.add(SentimentAnalysisSequential.analyzeLine(line, positiveWords, negativeWords));
        }

        outputArea.setText(String.join("\n", results));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SentimentAnalysisGUI().setVisible(true);
        });
    }
}
