package com.edutrack.academic.enums;

public enum Grade {
    O("O", 10, "Outstanding"),
    A_PLUS("A+", 9, "Excellent"),
    A("A", 8, "Very Good"),
    B_PLUS("B+", 7, "Good"),
    B("B", 6, "Above Average"),
    C("C", 5, "Average"),
    F("F", 0, "Fail");

    private final String code;
    private final int gradePoint;
    private final String description;

    Grade(String code, int gradePoint, String description) {
        this.code = code;
        this.gradePoint = gradePoint;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public int getGradePoint() {
        return gradePoint;
    }

    public String getDescription() {
        return description;
    }

    public static Grade calculateGrade(double marks) {
        if (marks >= 90.0) {
            return O;
        } else if (marks >= 80.0) {
            return A_PLUS;
        } else if (marks >= 70.0) {
            return A;
        } else if (marks >= 60.0) {
            return B_PLUS;
        } else if (marks >= 50.0) {
            return B;
        } else if (marks >= 40.0) {
            return C;
        } else {
            return F;
        }
    }
}
