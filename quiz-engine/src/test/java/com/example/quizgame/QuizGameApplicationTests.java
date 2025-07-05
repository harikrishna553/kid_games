package com.example.quizgame;

import com.example.quizgame.model.Quiz;
import com.example.quizgame.service.QuizService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuizGameApplicationTests {

    @Autowired
    private QuizService quizService;

    @Test
    void contextLoads() {
        assertNotNull(quizService);
    }

    @Test
    void testLoadQuizzes() {
        List<Quiz> quizzes = quizService.loadAllQuizzes();
        assertNotNull(quizzes);
        assertFalse(quizzes.isEmpty());
        
        for (Quiz quiz : quizzes) {
            assertNotNull(quiz.getTitle());
            assertNotNull(quiz.getQuestions());
            assertFalse(quiz.getQuestions().isEmpty());
        }
    }

    @Test
    void testGetQuizTitles() {
        List<String> titles = quizService.getAllQuizTitles();
        assertNotNull(titles);
        assertFalse(titles.isEmpty());
        
        assertTrue(titles.contains("Math Quiz"));
        assertTrue(titles.contains("Science Quiz"));
        assertTrue(titles.contains("History Quiz"));
        assertTrue(titles.contains("Geography Quiz"));
    }

    @Test
    void testGetQuestionsByTitles() {
        List<String> selectedTitles = List.of("Math Quiz", "Science Quiz");
        var questions = quizService.getQuestionsByTitles(selectedTitles, 5);
        
        assertNotNull(questions);
        assertEquals(5, questions.size());
        
        for (var question : questions) {
            assertNotNull(question.getQuestion());
            assertNotNull(question.getChoices());
            assertNotNull(question.getAnswerPositions());
            assertFalse(question.getChoices().isEmpty());
            assertFalse(question.getAnswerPositions().isEmpty());
        }
    }
}
