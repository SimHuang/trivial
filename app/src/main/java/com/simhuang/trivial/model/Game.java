package com.simhuang.trivial.model;

import java.util.List;

/**
 * This model represents a game in progress
 */
public class Game {

//    private String uid;
    private boolean inProgress;
    private String playerOne;
    private String playerTwo;
    private String gameTopic;
    private int betAmount;
    private int currentQuestion;
    private List<MashapeQuestion> questions;
    private List<Boolean> playerOneAnswers;
    private List<Boolean> playerTwoAnswers;

    public Game(boolean inProgress, String playerOne, String playerTwo,
                String gameTopic, int betAmount, int currentQuestion, List<MashapeQuestion> questions,
                List<Boolean> playerOneAnswers, List<Boolean> playerTwoAnswers) {

        this.inProgress = inProgress;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.gameTopic = gameTopic;
        this.betAmount = betAmount;
        this.currentQuestion = currentQuestion;
        this.questions = questions;
        this.playerOneAnswers = playerOneAnswers;
        this.playerTwoAnswers = playerTwoAnswers;
    }

    public Game() { }

//    public String getUid() {
//        return uid;
//    }
//
//    public void setUid(String uid) {
//        this.uid = uid;
//    }

    public boolean getInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public String getGameTopic() {
        return gameTopic;
    }

    public void setGameTopic(String gameTopic) {
        this.gameTopic = gameTopic;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public List<MashapeQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<MashapeQuestion> questions) {
        this.questions = questions;
    }

    public List<Boolean> getPlayerOneAnswers() {
        return playerOneAnswers;
    }

    public void setPlayerOneAnswers(List<Boolean> playerOneAnswers) {
        this.playerOneAnswers = playerOneAnswers;
    }

    public List<Boolean> getPlayerTwoAnswers() {
        return playerTwoAnswers;
    }

    public void setPlayerTwoAnswers(List<Boolean> playerTwoAnswers) {
        this.playerTwoAnswers = playerTwoAnswers;
    }
}
