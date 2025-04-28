package sentimentanalysis;

import sentimentanalysisgui.SentimentAnalysisParallel;
import sentimentanalysisgui.SentimentAnalysisSequential;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SentimentAnalysisGUI extends JFrame {
    private JTextField positiveFileField, negativeFileField, dataFileField;
    private JButton positiveBrowseBtn, negativeBrowseBtn, dataBrowseBtn, startBtn;
    private JRadioButton sequentialRadio, parallelRadio;
    private ButtonGroup modeGroup;
    private JLabel resultLabel;

    private File positiveFile, negativeFile, dataFile;

    public SentimentAnalysisGUI() {
        setTitle("Sentiment Analysis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(9, 1, 10, 10));

        // Positive file
        positiveFileField = new JTextField();
        positiveFileField.setEditable(false);
        positiveBrowseBtn = new JButton("Select Positive Words File");
        positiveBrowseBtn.addActionListener(e -> chooseFile(positiveFileField, "positive"));

        // Negative file
        negativeFileField = new JTextField();
        negativeFileField.setEditable(false);
        negativeBrowseBtn = new JButton("Select Negative Words File");
        negativeBrowseBtn.addActionListener(e -> chooseFile(negativeFileField, "negative"));

        // Data file
        dataFileField = new JTextField();
        dataFileField.setEditable(false);
        dataBrowseBtn = new JButton("Select Data File");
        dataBrowseBtn.addActionListener(e -> chooseFile(dataFileField, "data"));

        // Mode selection
        sequentialRadio = new JRadioButton("Sequential");
        parallelRadio = new JRadioButton("Parallel");
        sequentialRadio.setSelected(true);

        modeGroup = new ButtonGroup();
        modeGroup.add(sequentialRadio);
        modeGroup.add(parallelRadio);

        JPanel modePanel = new JPanel();
        modePanel.add(sequentialRadio);
        modePanel.add(parallelRadio);

        // Start button
        startBtn = new JButton("Start Analysis");
        startBtn.addActionListener(e -> startAnalysis());

        // Result label
        resultLabel = new JLabel(" ", SwingConstants.CENTER);

        // Adding components
        add(positiveFileField);
        add(positiveBrowseBtn);
        add(negativeFileField);
        add(negativeBrowseBtn);
        add(dataFileField);
        add(dataBrowseBtn);
        add(modePanel);
        add(startBtn);
        add(resultLabel);

        setVisible(true);
    }

    private void chooseFile(JTextField field, String type) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            field.setText(file.getAbsolutePath());
            if (type.equals("positive")) positiveFile = file;
            else if (type.equals("negative")) negativeFile = file;
            else if (type.equals("data")) dataFile = file;
        }
    }

    private void startAnalysis() {
        if (positiveFile == null || negativeFile == null || dataFile == null) {
            JOptionPane.showMessageDialog(this, "Please select all files!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isParallel = parallelRadio.isSelected();

        try {
            if (isParallel) {
                SentimentAnalysisParallel.AnalysisResult result = SentimentAnalysisParallel.analyze(new String[]{
                        positiveFile.getAbsolutePath(),
                        negativeFile.getAbsolutePath(),
                        dataFile.getAbsolutePath()
                });
                showResult(result.duration, result.positiveCount, result.negativeCount, result.neutralCount, "Parallel");
            } else {
                SentimentAnalysisSequential.AnalysisResult result = SentimentAnalysisSequential.analyze(new String[]{
                        positiveFile.getAbsolutePath(),
                        negativeFile.getAbsolutePath(),
                        dataFile.getAbsolutePath()
                });
                showResult(result.duration, result.positiveCount, result.negativeCount, result.neutralCount, "Sequential");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred during analysis.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showResult(long time, int positives, int negatives, int neutrals, String mode) {
        String message = "<html><center>Mode: " + mode + "<br>"
                + "Time: " + time + " ms<br>"
                + "Positive Sentences: " + positives + "<br>"
                + "Negative Sentences: " + negatives + "<br>"
                + "Neutral Sentences: " + neutrals + "<br>"
                + "Results saved to results_" + mode.toLowerCase() + ".txt</center></html>";

        resultLabel.setText(message);

        JOptionPane.showMessageDialog(this, message, "Analysis Completed", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SentimentAnalysisGUI::new);
    }
}
