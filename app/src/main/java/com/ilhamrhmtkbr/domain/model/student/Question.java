package com.ilhamrhmtkbr.domain.model.student;

public class Question {
    private final String id;
    private final String instructorCourseId;
    private final String question;
    private final String createdAt;
    private final String courseTitle;
    private final String answersId;
    private final String answer;
    private final String answerCreatedAt;

    public Question(String id, String instructorCourseId, String question, String createdAt, String courseTitle, String answersId, String answer, String answerCreatedAt) {
        this.id = id;
        this.instructorCourseId = instructorCourseId;
        this.question = question;
        this.createdAt = createdAt;
        this.courseTitle = courseTitle;
        this.answersId = answersId;
        this.answer = answer;
        this.answerCreatedAt = answerCreatedAt;
    }

    public String getId() {
        return id;
    }

    public String getInstructorCourseId() {
        return instructorCourseId;
    }

    public String getQuestion() {
        return question;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getAnswersId() {
        return answersId;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAnswerCreatedAt() {
        return answerCreatedAt;
    }
}