package com.edutrack.academic;

import com.edutrack.academic.enums.AcademicStatus;
import com.edutrack.academic.enums.Grade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GradeCalculationTest {

    @Test
    @DisplayName("Should correctly calculate grade and grade points based on marks scale")
    void testGradeCalculationScale() {
        // Marks >= 90 -> O (10)
        assertEquals(Grade.O, Grade.calculateGrade(95.0));
        assertEquals(10, Grade.O.getGradePoint());

        // Marks >= 80 -> A+ (9)
        assertEquals(Grade.A_PLUS, Grade.calculateGrade(85.0));
        assertEquals(9, Grade.A_PLUS.getGradePoint());

        // Marks >= 70 -> A (8)
        assertEquals(Grade.A, Grade.calculateGrade(75.0));
        assertEquals(8, Grade.A.getGradePoint());

        // Marks >= 60 -> B+ (7)
        assertEquals(Grade.B_PLUS, Grade.calculateGrade(65.0));
        assertEquals(7, Grade.B_PLUS.getGradePoint());

        // Marks >= 50 -> B (6)
        assertEquals(Grade.B, Grade.calculateGrade(55.0));
        assertEquals(6, Grade.B.getGradePoint());

        // Marks >= 40 -> C (5)
        assertEquals(Grade.C, Grade.calculateGrade(45.0));
        assertEquals(5, Grade.C.getGradePoint());

        // Marks < 40 -> F (0)
        assertEquals(Grade.F, Grade.calculateGrade(35.0));
        assertEquals(0, Grade.F.getGradePoint());
    }

    @Test
    @DisplayName("Should correctly determine Academic Status based on CGPA thresholds")
    void testAcademicStatusFromCgpa() {
        assertEquals(AcademicStatus.EXCELLENT, AcademicStatus.fromCgpa(8.8));
        assertEquals(AcademicStatus.GOOD, AcademicStatus.fromCgpa(7.5));
        assertEquals(AcademicStatus.SATISFACTORY, AcademicStatus.fromCgpa(6.2));
        assertEquals(AcademicStatus.NEEDS_IMPROVEMENT, AcademicStatus.fromCgpa(5.4));
    }
}
