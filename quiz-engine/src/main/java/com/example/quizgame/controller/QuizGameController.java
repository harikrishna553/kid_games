package com.example.quizgame.controller;

import com.example.quizgame.model.GameSession;
import com.example.quizgame.model.Question;
import com.example.quizgame.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class QuizGameController {

    private final GameService gameService;

    public QuizGameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<String> availableQuizzes = gameService.getAvailableQuizTitles();
        model.addAttribute("availableQuizzes", availableQuizzes);
        return "index";
    }

    @PostMapping("/start-game")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> startGame(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            String quizTitles = (String) request.get("quizToConsider");
            int numberOfQuestions = Integer.parseInt(request.get("numberOfQuestions").toString());
            int timePerQuestion = Integer.parseInt(request.get("timePerQuestion").toString());

            List<String> selectedQuizTitles = Arrays.stream(quizTitles.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());

            GameSession session = gameService.startNewGame(username, selectedQuizTitles, 
                                                         numberOfQuestions, timePerQuestion);

            Map<String, Object> response = new HashMap<>();
            response.put("sessionId", session.getSessionId());
            response.put("success", true);
            response.put("message", "Game started successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to start game: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/game/{sessionId}")
    public String gameView(@PathVariable String sessionId, 
                          @RequestParam(required = false) String username,
                          @RequestParam(required = false) Integer totalQuestions,
                          @RequestParam(required = false) Integer correctAnswers,
                          @RequestParam(required = false) Integer wrongAnswers,
                          @RequestParam(required = false) Double accuracy,
                          Model model) {
        GameSession session = gameService.getSession(sessionId);
        if (session == null) {
            System.out.println("ERROR: Session not found for ID: " + sessionId);
            return "redirect:/";
        }

        System.out.println("DEBUG: GameView accessed for session: " + sessionId + ", completed: " + session.isCompleted());
        model.addAttribute("session", session);
        
        if (session.isCompleted()) {
            // Check if we have final score data from URL parameters
            if (username != null && totalQuestions != null && correctAnswers != null && wrongAnswers != null && accuracy != null) {
                // Use the data from the final API response (URL parameters)
                Map<String, Object> finalScoreData = new HashMap<>();
                finalScoreData.put("username", username);
                finalScoreData.put("totalQuestions", totalQuestions);
                finalScoreData.put("correctAnswers", correctAnswers);
                finalScoreData.put("wrongAnswers", wrongAnswers);
                finalScoreData.put("accuracy", accuracy);
                model.addAttribute("finalScore", finalScoreData);
                
                System.out.println("=== DEBUG: Using Final Score from API Response ===");
                System.out.println("Username: " + username);
                System.out.println("Total Questions: " + totalQuestions);
                System.out.println("Correct Answers: " + correctAnswers);
                System.out.println("Wrong Answers: " + wrongAnswers);
                System.out.println("Accuracy: " + accuracy + "%");
                System.out.println("==================================================");
            } else {
                // Fallback to session data if no URL parameters
                System.out.println("=== DEBUG: Using Session Data (Fallback) ===");
                System.out.println("Session ID: " + session.getSessionId());
                System.out.println("Username: " + session.getUsername());
                System.out.println("Questions Attempted: " + session.getQuestionsAttempted());
                System.out.println("Correct Answers: " + session.getCorrectAnswersCount());
                System.out.println("Wrong Answers: " + session.getWrongAnswersCount());
                System.out.println("Accuracy: " + session.getAccuracyPercentage() + "%");
                System.out.println("==========================================");
            }
            return "game-summary";
        }
        
        return "game";
    }

    @GetMapping("/api/question/{sessionId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCurrentQuestion(@PathVariable String sessionId) {
        GameSession session = gameService.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        Question currentQuestion = session.getCurrentQuestion();
        if (currentQuestion == null || session.isCompleted()) {
            Map<String, Object> response = new HashMap<>();
            response.put("completed", true);
            response.put("totalQuestions", session.getQuestions() != null ? session.getQuestions().size() : 0);
            response.put("questionsAttempted", session.getQuestionsAttempted());
            response.put("correctAnswers", session.getCorrectAnswersCount());
            response.put("wrongAnswers", session.getWrongAnswersCount());
            response.put("accuracy", session.getAccuracyPercentage());
            return ResponseEntity.ok(response);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("question", currentQuestion);
        response.put("questionNumber", session.getCurrentQuestionIndex() + 1);
        response.put("totalQuestions", session.getQuestions().size());
        response.put("timePerQuestion", session.getTimePerQuestion());
        response.put("completed", false);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/submit-answer/{sessionId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitAnswer(@PathVariable String sessionId, 
                                                           @RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Integer> selectedAnswers = (List<Integer>) request.get("selectedAnswers");
            
            boolean isCorrect = gameService.submitAnswer(sessionId, selectedAnswers);
            GameSession session = gameService.getSession(sessionId);

            Map<String, Object> response = new HashMap<>();
            response.put("correct", isCorrect);
            response.put("completed", session.isCompleted());
            
            if (session.isCompleted()) {
                response.put("finalScore", createSummaryData(session));
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to submit answer: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/api/skip-question/{sessionId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> skipQuestion(@PathVariable String sessionId) {
        gameService.skipQuestion(sessionId);
        GameSession session = gameService.getSession(sessionId);

        Map<String, Object> response = new HashMap<>();
        response.put("skipped", true);
        response.put("completed", session.isCompleted());
        
        if (session.isCompleted()) {
            response.put("finalScore", createSummaryData(session));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/summary/{sessionId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getGameSummary(@PathVariable String sessionId) {
        GameSession session = gameService.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(createSummaryData(session));
    }

    @GetMapping("/api/answer-review/{sessionId}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAnswerReview(@PathVariable String sessionId) {
        GameSession session = gameService.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, Object>> reviewData = new ArrayList<>();
        List<Question> questions = session.getQuestions();
        List<Boolean> answers = session.getAnswers();
        List<List<Integer>> userAnswers = session.getUserAnswers();

        for (int i = 0; i < questions.size() && i < answers.size(); i++) {
            Map<String, Object> questionReview = new HashMap<>();
            questionReview.put("question", questions.get(i));
            questionReview.put("userCorrect", answers.get(i));
            questionReview.put("userAnswers", i < userAnswers.size() ? userAnswers.get(i) : new ArrayList<>());
            reviewData.add(questionReview);
        }

        return ResponseEntity.ok(reviewData);
    }

    private Map<String, Object> createSummaryData(GameSession session) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("username", session.getUsername());
        summary.put("totalQuestions", session.getQuestionsAttempted());
        summary.put("correctAnswers", session.getCorrectAnswersCount());
        summary.put("wrongAnswers", session.getWrongAnswersCount());
        summary.put("accuracy", Math.round(session.getAccuracyPercentage() * 100.0) / 100.0);
        return summary;
    }
}
