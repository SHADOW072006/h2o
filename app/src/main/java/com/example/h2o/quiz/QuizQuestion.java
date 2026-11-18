package com.example.h2o.quiz;

import java.util.List;

public class QuizQuestion {
    private final String question;
    private final List<String> options;
    private final int correctIndex;
    private final String explanation;

    public QuizQuestion(String question, List<String> options, int correctIndex, String explanation) {
        this.question = question;
        this.options = options;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public String getExplanation() {
        return explanation;
    }
}
