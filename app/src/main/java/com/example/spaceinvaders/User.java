package com.example.spaceinvaders;

public class User {

    private String nick;
    private Integer score;

    public User() {
    }

    public User(String nick, Integer score) {
        this.nick = nick;
        this.score = score;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
