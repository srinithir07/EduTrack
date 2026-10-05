package com.edutrack.academic.dto;

import java.util.ArrayList;
import java.util.List;

public class StudentAcademicSummary {

    private StudentResponse student;
    private Integer totalCredits;
    private Double cgpa;
    private String academicStatus;
    private List<SemesterGpaSummary> semesterSummaries = new ArrayList<>();
    private List<PerformanceResponse> allPerformances = new ArrayList<>();

    public StudentAcademicSummary() {
    }

    public StudentAcademicSummary(StudentResponse student, Integer totalCredits, Double cgpa, String academicStatus,
                                 List<SemesterGpaSummary> semesterSummaries, List<PerformanceResponse> allPerformances) {
        this.student = student;
        this.totalCredits = totalCredits;
        this.cgpa = cgpa;
        this.academicStatus = academicStatus;
        this.semesterSummaries = semesterSummaries;
        this.allPerformances = allPerformances;
    }

    public StudentResponse getStudent() {
        return student;
    }

    public void setStudent(StudentResponse student) {
        this.student = student;
    }

    public Integer getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Integer totalCredits) {
        this.totalCredits = totalCredits;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    public List<SemesterGpaSummary> getSemesterSummaries() {
        return semesterSummaries;
    }

    public void setSemesterSummaries(List<SemesterGpaSummary> semesterSummaries) {
        this.semesterSummaries = semesterSummaries;
    }

    public List<PerformanceResponse> getAllPerformances() {
        return allPerformances;
    }

    public void setAllPerformances(List<PerformanceResponse> allPerformances) {
        this.allPerformances = allPerformances;
    }
}
