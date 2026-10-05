package com.edutrack.academic;

import com.edutrack.academic.entity.Performance;
import com.edutrack.academic.entity.Semester;
import com.edutrack.academic.entity.Student;
import com.edutrack.academic.entity.Subject;
import com.edutrack.academic.repository.PerformanceRepository;
import com.edutrack.academic.repository.SemesterRepository;
import com.edutrack.academic.repository.StudentRepository;
import com.edutrack.academic.service.AcademicCalculationService;
import com.edutrack.academic.service.PerformanceService;
import com.edutrack.academic.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcademicCalculationServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SemesterRepository semesterRepository;

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private PerformanceService performanceService;

    @InjectMocks
    private AcademicCalculationService academicCalculationService;

    private Student testStudent;
    private Semester testSemester;
    private Subject subject1;
    private Subject subject2;

    @BeforeEach
    void setUp() {
        testStudent = new Student(1L, "21CS101", "Aarav Sharma", "aarav@example.com", "Computer Science", "2021-2025");
        testSemester = new Semester(1L, 1, "2024-2025");
        subject1 = new Subject(1L, "CS301", "Data Structures", 4);
        subject2 = new Subject(2L, "CS302", "DBMS", 3);
    }

    @Test
    @DisplayName("Should correctly calculate credit-weighted Semester GPA formula (9*4 + 8*3) / (4+3) = 8.57")
    void testCalculateSemesterGpaFormula() {
        // Subject 1: Grade Point 9 (A+), Credits 4 -> 36 points
        Performance p1 = new Performance(1L, testStudent, subject1, testSemester, 85.0, "A+", 9);
        // Subject 2: Grade Point 8 (A), Credits 3 -> 24 points
        Performance p2 = new Performance(2L, testStudent, subject2, testSemester, 75.0, "A", 8);

        List<Performance> performances = Arrays.asList(p1, p2);

        when(studentRepository.existsById(1L)).thenReturn(true);
        when(semesterRepository.existsById(1L)).thenReturn(true);
        when(performanceRepository.findByStudentIdAndSemesterId(1L, 1L)).thenReturn(performances);

        Double calculatedGpa = academicCalculationService.calculateSemesterGpa(1L, 1L);

        // Expected: (9*4 + 8*3) / (4+3) = (36 + 24) / 7 = 60 / 7 = 8.57
        assertEquals(8.57, calculatedGpa);
    }
}
