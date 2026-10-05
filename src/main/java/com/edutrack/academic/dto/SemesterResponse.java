package com.edutrack.academic.dto;

public class SemesterResponse {

    private Long id;
    private Integer semesterNumber;
    private String academicYear;

    public SemesterResponse() {
    }

    public SemesterResponse(Long id, Integer semesterNumber, String academicYear) {
        this.id = id;
        this.semesterNumber = semesterNumber;
        this.academicYear = academicYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(Integer semesterNumber) {
        this.semesterNumber = semesterNumber;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
}
