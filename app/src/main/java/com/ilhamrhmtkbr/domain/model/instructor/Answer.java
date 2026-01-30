package com.ilhamrhmtkbr.domain.model.instructor;

public class Answer {
    private String title;
    private String questionId;
    private String question;
    private String questionCreatedAt;
    private String answerId;
    private String answer;
    private String student;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionCreatedAt() {
        return questionCreatedAt;
    }

    public void setQuestionCreatedAt(String questionCreatedAt) {
        this.questionCreatedAt = questionCreatedAt;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }
}
