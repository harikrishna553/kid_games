package com.example.quizgame.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GameSession {
    
    private String sessionId;
    private String username;
    private List<String> selectedQuizTitles;
    private int numberOfQuestions;
    private int timePerQuestion;
    private List<Question> questions;
    private int currentQuestionIndex;
    private List<Boolean> answers;
    private List<List<Integer>> userAnswers;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean completed;

    // Default constructor
    public GameSession() {
        this.answers = new ArrayList<>();
        this.userAnswers = new ArrayList<>();
        this.currentQuestionIndex = 0;
        this.completed = false;
    }

    // Constructor
    public GameSession(String sessionId, String username, List<String> selectedQuizTitles, 
                      int numberOfQuestions, int timePerQuestion) {
        this();
        this.sessionId = sessionId;
        this.username = username;
        this.selectedQuizTitles = selectedQuizTitles;
        this.numberOfQuestions = numberOfQuestions;
        this.timePerQuestion = timePerQuestion;
        this.startTime = LocalDateTime.now();
    }

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getSelectedQuizTitles() {
        return selectedQuizTitles;
    }

    public void setSelectedQuizTitles(List<String> selectedQuizTitles) {
        this.selectedQuizTitles = selectedQuizTitles;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public int getTimePerQuestion() {
        return timePerQuestion;
    }

    public void setTimePerQuestion(int timePerQuestion) {
        this.timePerQuestion = timePerQuestion;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public List<Boolean> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Boolean> answers) {
        this.answers = answers;
    }

    public List<List<Integer>> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<List<Integer>> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        if (completed && endTime == null) {
            this.endTime = LocalDateTime.now();
        }
    }

    // Helper methods
    public Question getCurrentQuestion() {
        if (questions != null && currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean hasNextQuestion() {
        return questions != null && currentQuestionIndex < questions.size() - 1;
    }

    public void nextQuestion() {
        if (hasNextQuestion()) {
            currentQuestionIndex++;
        }
    }

    public void addAnswer(boolean correct, List<Integer> userAnswer) {
        answers.add(correct);
        userAnswers.add(new ArrayList<>(userAnswer));
    }

    public int getCorrectAnswersCount() {
        return (int) answers.stream().mapToInt(b -> b ? 1 : 0).sum();
    }

    public int getWrongAnswersCount() {
        return answers.size() - getCorrectAnswersCount();
    }

    public double getAccuracyPercentage() {
        if (answers.isEmpty()) {
            return 0.0;
        }
        return (double) getCorrectAnswersCount() / answers.size() * 100.0;
    }

    public int getQuestionsAttempted() {
        return answers.size();
    }
}
