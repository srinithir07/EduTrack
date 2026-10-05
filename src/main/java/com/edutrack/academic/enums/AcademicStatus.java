package com.edutrack.academic.enums;

public enum AcademicStatus {
    EXCELLENT("Excellent"),
    GOOD("Good"),
    SATISFACTORY("Satisfactory"),
    NEEDS_IMPROVEMENT("Needs Improvement");

    private final String displayName;

    AcademicStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AcademicStatus fromCgpa(double cgpa) {
        if (cgpa >= 8.5) {
            return EXCELLENT;
        } else if (cgpa >= 7.0) {
            return GOOD;
        } else if (cgpa >= 6.0) {
            return SATISFACTORY;
        } else {
            return NEEDS_IMPROVEMENT;
        }
    }
}
