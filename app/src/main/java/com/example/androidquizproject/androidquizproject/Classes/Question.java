package com.example.androidquizproject.androidquizproject.Classes;


public class Question {
    private String AnswerA;
    private String AnswerB;
    private String AnswerC;
    private String AnswerD;
    private String CorrectAnswer;
    private String PictureAddress;

    public Question(){}

    public Question(String answerA, String answerB, String answerC, String answerD,                 //בנאי של שאלה
                    String correctAnswer,String pictureAddress) {
        AnswerA = answerA;
        AnswerB = answerB;
        AnswerC = answerC;
        AnswerD = answerD;
        CorrectAnswer = correctAnswer;
        PictureAddress = pictureAddress;
    }

    public String getAnswerA() {
        return AnswerA;
    }

    public void setAnswerA(String answerA) {
        AnswerA = answerA;
    }

    public String getAnswerB() {
        return AnswerB;
    }

    public void setAnswerB(String answerB) {
        AnswerB = answerB;
    }

    public String getAnswerC() {
        return AnswerC;
    }

    public void setAnswerC(String answerC) {
        AnswerC = answerC;
    }

    public String getAnswerD() {
        return AnswerD;
    }

    public void setAnswerD(String answerD) {
        AnswerD = answerD;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getPictureAddress() {
        return PictureAddress;
    }

    public void setPictureAddress(String pictureAddress) {
        PictureAddress = pictureAddress;
    }


}
