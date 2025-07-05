package com.example.quizgame.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Question {
    
    private String id;
    private String question;
    private String hint;
    private String answerType;
    private List<String> choices;
    private List<Integer> answerPositions;
    private List<String> explanations;
    private String image;
    private List<String> tags;

    // Default constructor
    public Question() {}

    // Constructor with required fields
    public Question(String id, String question, List<String> choices, List<Integer> answerPositions) {
        this.id = id;
        this.question = question;
        this.choices = choices;
        this.answerPositions = answerPositions;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public List<Integer> getAnswerPositions() {
        return answerPositions;
    }

    public void setAnswerPositions(List<Integer> answerPositions) {
        this.answerPositions = answerPositions;
    }

    public List<String> getExplanations() {
        return explanations;
    }

    public void setExplanations(List<String> explanations) {
        this.explanations = explanations;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isMultipleChoice() {
        return "multi".equalsIgnoreCase(answerType);
    }

    public boolean isCorrectAnswer(List<Integer> selectedPositions) {
        if (selectedPositions == null || answerPositions == null) {
            return false;
        }
        return selectedPositions.size() == answerPositions.size() && 
               selectedPositions.containsAll(answerPositions);
    }
}
