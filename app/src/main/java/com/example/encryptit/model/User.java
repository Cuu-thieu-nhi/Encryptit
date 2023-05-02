package com.example.encryptit.model;

import java.io.Serializable;

public class User implements Serializable {
    String userName;
    String passWord;
    String hint;

    public User() {
    }

    public User(String userName, String passWord, String hint) {
        this.userName = userName;
        this.passWord = passWord;
        this.hint = hint;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public String toString() {
        return "User{" + "userName='" + userName + '\'' + ", passWord='" + passWord + '\'' + ", hint='" + hint + '\'' + '}';
    }
}
