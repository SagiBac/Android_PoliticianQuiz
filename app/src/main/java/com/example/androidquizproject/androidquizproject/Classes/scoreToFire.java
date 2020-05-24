package com.example.androidquizproject.androidquizproject.Classes;

public class scoreToFire {                                                                          //מחלקת תוצאות המכילה משתמש ותוצאה לצורך העברה למסד
    private String userName;
    private int score;

    public scoreToFire() {
    }

    public scoreToFire(String userName, int score) {
        this.userName = userName;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return new String("name: "+this.userName+"\n"+"Score: "+this.score);
    }
}

