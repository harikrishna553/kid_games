package com.example.kidsspellinggame.model;

public class CheckRequest {
    private String userInput;
    private String originalWord;

    public CheckRequest() {}

    public CheckRequest(String userInput, String originalWord) {
        this.userInput = userInput;
        this.originalWord = originalWord;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public String getOriginalWord() {
        return originalWord;
    }

    public void setOriginalWord(String originalWord) {
        this.originalWord = originalWord;
    }
}
