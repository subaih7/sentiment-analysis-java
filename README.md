# Sentiment Analysis using Multi-Threading in Java

## 📌 Project Overview

This project is a **Sentiment Analysis Tool** that processes text data using **multi-threading in Java**. Users can upload three files:

1. **Positive Words File** → Contains positive words (one per line).
2. **Negative Words File** → Contains negative words (one per line).
3. **Text Data File** → Contains multiple lines of text to be analyzed.

The program analyzes each text entry and classifies it as **Positive, Negative, or Neutral** based on the occurrence of words from the positive and negative word lists.

---

## ⚡ Features

✅ Supports multi-threaded processing for faster analysis.
✅ Allows users to upload custom word lists.
✅ Outputs the results into a new file with sentiment classifications.
✅ Simple and efficient implementation using Java.



### **3️⃣ Compile and Run**

```bash
javac SentimentAnalysis.java
java SentimentAnalysis positive_words.txt negative_words.txt data.txt
```

---

## 📂 File Structure

```
📂 sentiment-analysis-java
├── 📄 SentimentAnalysis.java   # Main Java program
├── 📄 positive_words.txt       # List of positive words
├── 📄 negative_words.txt       # List of negative words
├── 📄 data.txt                 # File containing text to analyze
├── 📄 results_parallel.txt     # Output file with sentiment parallel classifications
├── 📄 results_sequential.txt     # Output file with sentiment sequential classifications
└── 📄 README.md                # Project documentation
```

---

##  How It Works

1. **Load positive and negative words** from the provided files.
2. **Read the text data file** and distribute the analysis task among multiple threads.
3. **Each thread processes a chunk of text**, counting occurrences of positive and negative words.
4. **Classifies each text** as Positive 😀, Negative 😞, or Neutral 😐.
5. **Saves the results** in `results.txt`.

---

## Example

### **Input:**

```
I love this product, it is amazing!
This is the worst service ever.
It's okay, not too bad but not great either.
```

### **Output:**

```
I love this product, it is amazing! -> Positive 😀
This is the worst service ever. -> Negative 😞
It's okay, not too bad but not great either. -> Neutral 😐
```

---

## 🔄 Future Enhancements

🔹 Support for text file formats**.
🔹 Improve accuracy using **Natural Language Processing (NLP)** techniques.

