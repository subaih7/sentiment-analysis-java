package sentimentanalysisgui;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SentimentAnalysisParallel {

    public static class AnalysisResult {
        public long duration;
        public int positiveCount;
        public int negativeCount;
        public int neutralCount;
    }

    public static AnalysisResult analyze(String[] args) throws IOException, InterruptedException {
        AnalysisResult analysisResult = new AnalysisResult();

        if (args.length != 3) {
            System.out.println("Usage: java SentimentAnalysisParallel positive_words.txt negative_words.txt data.txt");
            return analysisResult;
        }

        Set<String> positiveWords = loadWords(args[0]);
        Set<String> negativeWords = loadWords(args[1]);
        List<String> lines = loadLines(args[2]);

        List<String> results = Collections.synchronizedList(new ArrayList<>());

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        int posCount = 0, negCount = 0, neutralCount = 0;
        AtomicInteger posCounter = new AtomicInteger(0);
        AtomicInteger negCounter = new AtomicInteger(0);
        AtomicInteger neutralCounter = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        for (String line : lines) {
            executor.submit(() -> {
                String analyzed = analyzeLine(line, positiveWords, negativeWords);
                results.add(analyzed);

                if (analyzed.endsWith("Positive 😀")) posCounter.incrementAndGet();
                else if (analyzed.endsWith("Negative 😞")) negCounter.incrementAndGet();
                else neutralCounter.incrementAndGet();
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        long endTime = System.currentTimeMillis();
        saveResults(results);

        analysisResult.duration = endTime - startTime;
        analysisResult.positiveCount = posCounter.get();
        analysisResult.negativeCount = negCounter.get();
        analysisResult.neutralCount = neutralCounter.get();

        return analysisResult;
    }

    static Set<String> loadWords(String filename) throws IOException {
        Set<String> words = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        }
        return words;
    }

    static List<String> loadLines(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    static String analyzeLine(String line, Set<String> positiveWords, Set<String> negativeWords) {
        int pos = 0, neg = 0;
        for (String word : line.toLowerCase().split("\\W+")) {
            if (positiveWords.contains(word)) pos++;
            if (negativeWords.contains(word)) neg++;
        }
        String sentiment = "Neutral 😐";
        if (pos > neg) sentiment = "Positive 😀";
        else if (neg > pos) sentiment = "Negative 😞";
        return line + " -> " + sentiment;
    }

    static void saveResults(List<String> results) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results_parallel.txt"))) {
            for (String result : results) {
                writer.write(result);
                writer.newLine();
            }
        }
    }
}
