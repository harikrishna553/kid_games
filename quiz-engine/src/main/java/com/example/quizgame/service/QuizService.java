package com.example.quizgame.service;

import com.example.quizgame.model.Quiz;
import com.example.quizgame.model.Question;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public QuizService(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    public List<Quiz> loadAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        
        try {
            // Get all JSON files from the quiz_documents directory
            Resource qaDirectory = resourceLoader.getResource("classpath:quiz_documents/");
            if (qaDirectory.exists()) {
                // Use PathMatchingResourcePatternResolver to find all JSON files
                PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource[] quizFiles = resolver.getResources("classpath:quiz_documents/*.json");
                
                logger.info("Found {} quiz files in qa directory", quizFiles.length);
                
                for (Resource quizFile : quizFiles) {
                    try {
                        Quiz quiz = loadQuizFromResource(quizFile);
                        if (quiz != null && quiz.getQuestions() != null && !quiz.getQuestions().isEmpty()) {
                            quizzes.add(quiz);
                            logger.info("Loaded quiz: {} with {} questions from {}", 
                                quiz.getTitle(), quiz.getQuestions().size(), quizFile.getFilename());
                        } else {
                            logger.warn("Skipped empty or invalid quiz file: {}", quizFile.getFilename());
                        }
                    } catch (Exception e) {
                        logger.warn("Failed to load quiz file: {}", quizFile.getFilename(), e);
                    }
                }
            } else {
                logger.warn("Quiz directory 'classpath:quiz_documents/' not found");
            }
        } catch (Exception e) {
            logger.error("Error scanning for quiz files", e);
        }

        logger.info("Successfully loaded {} quizzes", quizzes.size());
        return quizzes;
    }

    private Quiz loadQuizFromResource(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, Quiz.class);
        } catch (IOException e) {
            logger.error("Error reading quiz file", e);
            return null;
        }
    }

    public List<String> getAllQuizTitles() {
        return loadAllQuizzes().stream()
                .map(Quiz::getTitle)
                .collect(Collectors.toList());
    }

    public List<Question> getQuestionsByTitles(List<String> selectedTitles, int numberOfQuestions) {
        List<Quiz> allQuizzes = loadAllQuizzes();
        List<Question> selectedQuestions = new ArrayList<>();

        // Filter quizzes by selected titles
        List<Quiz> selectedQuizzes = allQuizzes.stream()
                .filter(quiz -> selectedTitles.contains(quiz.getTitle()))
                .collect(Collectors.toList());

        // Collect all questions from selected quizzes
        for (Quiz quiz : selectedQuizzes) {
            selectedQuestions.addAll(quiz.getQuestions());
        }

        // Shuffle the questions
        Collections.shuffle(selectedQuestions);

        // Return the requested number of questions
        return selectedQuestions.stream()
                .limit(numberOfQuestions)
                .collect(Collectors.toList());
    }

    public Quiz getQuizByTitle(String title) {
        return loadAllQuizzes().stream()
                .filter(quiz -> quiz.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }
}
