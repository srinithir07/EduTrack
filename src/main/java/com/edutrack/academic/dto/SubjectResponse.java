package com.edutrack.academic.dto;

public class SubjectResponse {

    private Long id;
    private String subjectCode;
    private String subjectName;
    private Integer credits;

    public SubjectResponse() {
    }

    public SubjectResponse(Long id, String subjectCode, String subjectName, Integer credits) {
        this.id = id;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.credits = credits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }
}
