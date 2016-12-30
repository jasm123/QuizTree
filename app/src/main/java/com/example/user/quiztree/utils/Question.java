package com.example.user.quiztree.utils;


public class Question {
    public String questionText;
    public String option1;
    public String option2;
    public String option3;
    public String option4;
    public String correctAns;
    public boolean marksGiven;

    public Question() {

    }

    public Question(String questionText, String option1, String option2, String option3, String option4, String correctAns) {
        this.questionText = questionText;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAns = correctAns;
        marksGiven = false;
    }

    public boolean isCorrect(String selectedAns) {
        return selectedAns.equals(correctAns);
    }
}
