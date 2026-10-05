package com.edutrack.academic.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SemesterRequest {

    @NotNull(message = "Semester number is required")
    @Min(value = 1, message = "Semester number must be at least 1")
    @Max(value = 8, message = "Semester number cannot exceed 8")
    private Integer semesterNumber;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    public SemesterRequest() {
    }

    public SemesterRequest(Integer semesterNumber, String academicYear) {
        this.semesterNumber = semesterNumber;
        this.academicYear = academicYear;
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
