package com.simhuang.trivial.model;

import java.util.List;

public class MashapeQuestion {

    private String questionType;
    private String question;
    private String answer;
    private String category;
    List<String> chooices;

    public MashapeQuestion(String questionType, String question, String answer, String category, List<String> chooices) {
        this.questionType = questionType;
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.chooices = chooices;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getChooices() {
        return chooices;
    }

    public void setChoices(List<String> chooices) {
        this.chooices = chooices;
    }
}
