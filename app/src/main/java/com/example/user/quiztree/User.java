package com.example.user.quiztree;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String name;
    public String grade;
    public int score_m_1;//chapter 1 maths
    public int score_m_2;//chapter 2 maths
    public int score_s_1;//chapter 1 science
    public int score_s_2;//chapter 2 science

    public String email;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name,String grade, String email) {
        this.name = name;
        this.grade=grade;
        this.email = email;
        score_m_1=0;
        score_m_2=0;
        score_s_1=0;
        score_s_2=0;
    }
}
