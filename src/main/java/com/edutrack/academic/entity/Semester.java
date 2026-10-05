package com.edutrack.academic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "semesters")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "semester_number", nullable = false)
    private Integer semesterNumber;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    public Semester() {
    }

    public Semester(Integer semesterNumber, String academicYear) {
        this.semesterNumber = semesterNumber;
        this.academicYear = academicYear;
    }

    public Semester(Long id, Integer semesterNumber, String academicYear) {
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
