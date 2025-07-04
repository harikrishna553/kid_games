package com.example.kidsspellinggame.service;

import com.example.kidsspellinggame.model.Word;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class SpellingGameService {
    
    private final List<String> words;
    private final Random random;

    public SpellingGameService() {
        this.random = new Random();
        this.words = loadWordsFromFile();
    }

    private List<String> loadWordsFromFile() {
        List<String> wordList = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource("words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line = reader.readLine();
            if (line != null) {
                String[] wordsArray = line.split(",");
                for (String word : wordsArray) {
                    wordList.add(word.trim().toLowerCase());
                }
            }
            reader.close();
        } catch (IOException e) {
            // Fallback to default words if file reading fails
            System.err.println("Could not load words from file, using default words: " + e.getMessage());
            wordList.addAll(Arrays.asList("device", "computer", "keyboard", "monitor", "software"));
        }
        return wordList;
    }

    public Word getRandomWord() {
        String selectedWord = words.get(random.nextInt(words.size()));
        String wordWithBlanks = createWordWithBlanks(selectedWord);
        String hint = generateHint(selectedWord);
        
        return new Word(selectedWord, wordWithBlanks, hint);
    }

    public boolean checkAnswer(String userInput, String originalWord) {
        if (userInput == null || originalWord == null) {
            return false;
        }
        return userInput.trim().toLowerCase().equals(originalWord.toLowerCase());
    }

    private String createWordWithBlanks(String word) {
        if (word == null || word.length() <= 2) {
            return word;
        }
        
        char[] wordArray = word.toCharArray();
        int wordLength = word.length();
        
        // Calculate number of blanks (30-50% of word length, minimum 1, maximum length-1)
        int numBlanks = Math.max(1, Math.min(wordLength - 1, (int)(wordLength * (0.3 + random.nextDouble() * 0.2))));
        
        // Create a set of positions to blank out
        Set<Integer> blankPositions = new HashSet<>();
        
        // Always keep the first letter visible for better readability
        while (blankPositions.size() < numBlanks) {
            int position = 1 + random.nextInt(wordLength - 1); // Start from index 1 to keep first letter
            blankPositions.add(position);
        }
        
        // Replace selected positions with dashes
        for (int position : blankPositions) {
            wordArray[position] = '-';
        }
        
        return new String(wordArray).toUpperCase();
    }

    private String generateHint(String word) {
        Map<String, String> hints = new HashMap<>();
        
        // Technology-related hints
        hints.put("device", "An electronic gadget or tool");
        hints.put("computer", "A machine that processes data and information");
        hints.put("keyboard", "You type on this with your fingers");
        hints.put("monitor", "The screen that displays information");
        hints.put("software", "Programs and applications that run on computers");
        hints.put("hardware", "The physical parts of a computer");
        hints.put("internet", "A global network connecting computers worldwide");
        hints.put("website", "A collection of web pages on the internet");
        hints.put("database", "A organized collection of stored information");
        hints.put("network", "Connected computers that can share information");
        hints.put("printer", "A device that puts text and images on paper");
        hints.put("scanner", "A device that copies documents into digital form");
        hints.put("speaker", "A device that produces sound");
        hints.put("camera", "A device used to take pictures");
        hints.put("tablet", "A portable touchscreen computer");
        hints.put("laptop", "A portable computer you can carry");
        hints.put("desktop", "A computer that sits on a desk");
        hints.put("mobile", "A portable phone that fits in your pocket");
        hints.put("server", "A powerful computer that serves other computers");
        hints.put("router", "A device that directs internet traffic");
        hints.put("email", "Electronic messages sent over the internet");
        hints.put("password", "A secret code to protect your accounts");
        hints.put("username", "Your unique name for logging into accounts");
        hints.put("download", "Getting files from the internet to your device");
        hints.put("upload", "Sending files from your device to the internet");
        hints.put("browser", "Software used to view websites");
        hints.put("search", "Looking for information online");
        hints.put("document", "A file containing text or information");
        hints.put("folder", "A container that organizes files");
        hints.put("file", "A digital document stored on a computer");
        
        return hints.getOrDefault(word.toLowerCase(), "A technology-related word");
    }
}
