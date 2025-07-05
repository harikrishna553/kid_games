package com.example.quizgame.model;

import java.util.List;

public class Quiz {
    
    private String title;
    private String subject;
    private String difficulty;
    private List<Question> questions;

    // Default constructor
    public Quiz() {}

    // Constructor
    public Quiz(String title, String subject, String difficulty, List<Question> questions) {
        this.title = title;
        this.subject = subject;
        this.difficulty = difficulty;
        this.questions = questions;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
