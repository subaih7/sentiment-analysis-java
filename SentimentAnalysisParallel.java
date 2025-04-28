import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class SentimentAnalysisParallel {

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 3) {
            System.out.println("Usage: java SentimentAnalysisParallel positive_words.txt negative_words.txt data.txt");
            return;
        }

        Set<String> positiveWords = loadWords(args[0]);
        Set<String> negativeWords = loadWords(args[1]);
        List<String> lines = loadLines(args[2]);
        
        List<String> results = Collections.synchronizedList(new ArrayList<>());
        int numThreads = 4;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        long startTime = System.currentTimeMillis();
        
        for (String line : lines) {
            executor.submit(() -> {
                results.add(analyzeLine(line, positiveWords, negativeWords));
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        long endTime = System.currentTimeMillis();
        saveResults(results);
        
        System.out.println("Parallel Analysis Done in " + (endTime - startTime) + " ms");
    }

    static Set<String> loadWords(String filename) throws IOException {
        Set<String> words = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            words.add(line.trim().toLowerCase());
        }
        reader.close();
        return words;
    }

    static List<String> loadLines(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
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
        BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"));
        for (String result : results) {
            writer.write(result);
            writer.newLine();
        }
        writer.close();
    }
}
