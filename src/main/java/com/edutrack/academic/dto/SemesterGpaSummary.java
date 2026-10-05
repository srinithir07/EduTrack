package com.edutrack.academic.dto;

import java.util.ArrayList;
import java.util.List;

public class SemesterGpaSummary {

    private Long semesterId;
    private Integer semesterNumber;
    private String academicYear;
    private Double gpa;
    private Integer totalCredits;
    private List<PerformanceResponse> performances = new ArrayList<>();

    public SemesterGpaSummary() {
    }

    public SemesterGpaSummary(Long semesterId, Integer semesterNumber, String academicYear, Double gpa, Integer totalCredits, List<PerformanceResponse> performances) {
        this.semesterId = semesterId;
        this.semesterNumber = semesterNumber;
        this.academicYear = academicYear;
        this.gpa = gpa;
        this.totalCredits = totalCredits;
        this.performances = performances;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
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

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public Integer getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Integer totalCredits) {
        this.totalCredits = totalCredits;
    }

    public List<PerformanceResponse> getPerformances() {
        return performances;
    }

    public void setPerformances(List<PerformanceResponse> performances) {
        this.performances = performances;
    }
}
