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

	private final List<WordHint> wordHints;
	private final Random random;

	public SpellingGameService() {
		this.random = new Random();
		this.wordHints = loadWordsFromFile();
	}

	private static class WordHint {
		private final String word;
		private final String hint;

		public WordHint(String word, String hint) {
			this.word = word;
			this.hint = hint;
		}

		public String getWord() {
			return word;
		}

		public String getHint() {
			return hint;
		}
	}

	private List<WordHint> loadWordsFromFile() {
		List<WordHint> wordHintList = new ArrayList<>();
		try {
			ClassPathResource resource = new ClassPathResource("words.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (!line.isEmpty() && line.contains(":")) {
					String[] parts = line.split(":", 2);
					if (parts.length == 2) {
						String word = parts[0].trim().toLowerCase();
						String hint = parts[1].trim();
						wordHintList.add(new WordHint(word, hint));
					}
				}
			}

			reader.close();
		} catch (IOException e) {
			// Fallback to default words if file reading fails
			System.err.println("Could not load words from file, using default words: " + e.getMessage());
			wordHintList.add(new WordHint("device", "An electronic gadget or tool"));
			wordHintList.add(new WordHint("computer", "A machine that processes data and information"));
			wordHintList.add(new WordHint("keyboard", "You type on this with your fingers"));
			wordHintList.add(new WordHint("monitor", "The screen that displays information"));
			wordHintList.add(new WordHint("software", "Programs and applications that run on computers"));
		}
		return wordHintList;
	}

	public Word getRandomWord() {
		WordHint selectedWordHint = wordHints.get(random.nextInt(wordHints.size()));
		String selectedWord = selectedWordHint.getWord();
		String wordWithBlanks = createWordWithBlanks(selectedWord);
		String hint = selectedWordHint.getHint();

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

		// Calculate number of blanks (30-50% of word length, minimum 1, maximum
		// length-1)
		int numBlanks = Math.max(1, Math.min(wordLength - 1, (int) (wordLength * (0.3 + random.nextDouble() * 0.2))));

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
}
