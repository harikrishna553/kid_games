package com.example.quizgame.service;

import com.example.quizgame.model.GameSession;
import com.example.quizgame.model.Question;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final Map<String, GameSession> activeSessions = new ConcurrentHashMap<>();
    private final QuizService quizService;

    public GameService(QuizService quizService) {
        this.quizService = quizService;
    }

    public GameSession startNewGame(String username, List<String> selectedQuizTitles, 
                                   int numberOfQuestions, int timePerQuestion) {
        String sessionId = UUID.randomUUID().toString();
        GameSession session = new GameSession(sessionId, username, selectedQuizTitles, 
                                            numberOfQuestions, timePerQuestion);

        // Load questions for the game
        List<Question> questions = quizService.getQuestionsByTitles(selectedQuizTitles, numberOfQuestions);
        session.setQuestions(questions);

        activeSessions.put(sessionId, session);
        return session;
    }

    public GameSession getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public boolean submitAnswer(String sessionId, List<Integer> selectedAnswers) {
        GameSession session = getSession(sessionId);
        if (session == null || session.isCompleted()) {
            return false;
        }

        Question currentQuestion = session.getCurrentQuestion();
        if (currentQuestion == null) {
            return false;
        }

        boolean isCorrect = currentQuestion.isCorrectAnswer(selectedAnswers);
        session.addAnswer(isCorrect, selectedAnswers);

        // Debug logging
        System.out.println("DEBUG: Answer submitted for session " + sessionId);
        System.out.println("DEBUG: Question: " + currentQuestion.getQuestion());
        System.out.println("DEBUG: Selected answers: " + selectedAnswers);
        System.out.println("DEBUG: Is correct: " + isCorrect);
        System.out.println("DEBUG: Total answers now: " + session.getQuestionsAttempted());

        if (session.hasNextQuestion()) {
            session.nextQuestion();
        } else {
            session.setCompleted(true);
            System.out.println("DEBUG: Game completed! Final stats:");
            System.out.println("DEBUG: Questions attempted: " + session.getQuestionsAttempted());
            System.out.println("DEBUG: Correct answers: " + session.getCorrectAnswersCount());
            System.out.println("DEBUG: Wrong answers: " + session.getWrongAnswersCount());
            System.out.println("DEBUG: Accuracy: " + session.getAccuracyPercentage() + "%");
        }

        return isCorrect;
    }

    public void endSession(String sessionId) {
        GameSession session = getSession(sessionId);
        if (session != null) {
            session.setCompleted(true);
            // Optionally remove from active sessions after some time
            // activeSessions.remove(sessionId);
        }
    }

    public List<String> getAvailableQuizTitles() {
        return quizService.getAllQuizTitles();
    }

    public void skipQuestion(String sessionId) {
        GameSession session = getSession(sessionId);
        if (session != null && !session.isCompleted()) {
            // Add a wrong answer for skipped question
            session.addAnswer(false, List.of());
            
            if (session.hasNextQuestion()) {
                session.nextQuestion();
            } else {
                session.setCompleted(true);
            }
        }
    }

    public void removeSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public int getActiveSessionsCount() {
        return activeSessions.size();
    }
}
