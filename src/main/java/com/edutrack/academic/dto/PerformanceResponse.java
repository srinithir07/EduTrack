package com.edutrack.academic.dto;

public class PerformanceResponse {

    private Long id;
    private Long studentId;
    private String studentRegisterNumber;
    private String studentName;
    private Long subjectId;
    private String subjectCode;
    private String subjectName;
    private Integer subjectCredits;
    private Long semesterId;
    private Integer semesterNumber;
    private String academicYear;
    private Double marks;
    private String grade;
    private Integer gradePoint;

    public PerformanceResponse() {
    }

    public PerformanceResponse(Long id, Long studentId, String studentRegisterNumber, String studentName,
                               Long subjectId, String subjectCode, String subjectName, Integer subjectCredits,
                               Long semesterId, Integer semesterNumber, String academicYear,
                               Double marks, String grade, Integer gradePoint) {
        this.id = id;
        this.studentId = studentId;
        this.studentRegisterNumber = studentRegisterNumber;
        this.studentName = studentName;
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectCredits = subjectCredits;
        this.semesterId = semesterId;
        this.semesterNumber = semesterNumber;
        this.academicYear = academicYear;
        this.marks = marks;
        this.grade = grade;
        this.gradePoint = gradePoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentRegisterNumber() {
        return studentRegisterNumber;
    }

    public void setStudentRegisterNumber(String studentRegisterNumber) {
        this.studentRegisterNumber = studentRegisterNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
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

    public Integer getSubjectCredits() {
        return subjectCredits;
    }

    public void setSubjectCredits(Integer subjectCredits) {
        this.subjectCredits = subjectCredits;
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

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(Integer gradePoint) {
        this.gradePoint = gradePoint;
    }
}
