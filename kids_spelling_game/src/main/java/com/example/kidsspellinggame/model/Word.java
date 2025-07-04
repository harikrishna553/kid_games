package com.example.kidsspellinggame.model;

public class Word {
    private String original;
    private String withBlanks;
    private String hint;

    public Word() {}

    public Word(String original, String withBlanks, String hint) {
        this.original = original;
        this.withBlanks = withBlanks;
        this.hint = hint;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getWithBlanks() {
        return withBlanks;
    }

    public void setWithBlanks(String withBlanks) {
        this.withBlanks = withBlanks;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
