package com.edutrack.academic.service;

import com.edutrack.academic.dto.PerformanceResponse;
import com.edutrack.academic.dto.SemesterGpaSummary;
import com.edutrack.academic.dto.StudentAcademicSummary;
import com.edutrack.academic.dto.StudentResponse;
import com.edutrack.academic.entity.Performance;
import com.edutrack.academic.entity.Semester;
import com.edutrack.academic.entity.Student;
import com.edutrack.academic.enums.AcademicStatus;
import com.edutrack.academic.exception.ResourceNotFoundException;
import com.edutrack.academic.repository.PerformanceRepository;
import com.edutrack.academic.repository.SemesterRepository;
import com.edutrack.academic.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AcademicCalculationService {

    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;
    private final PerformanceRepository performanceRepository;
    private final StudentService studentService;
    private final PerformanceService performanceService;

    public AcademicCalculationService(StudentRepository studentRepository,
                                     SemesterRepository semesterRepository,
                                     PerformanceRepository performanceRepository,
                                     StudentService studentService,
                                     PerformanceService performanceService) {
        this.studentRepository = studentRepository;
        this.semesterRepository = semesterRepository;
        this.performanceRepository = performanceRepository;
        this.studentService = studentService;
        this.performanceService = performanceService;
    }

    @Transactional(readOnly = true)
    public Double calculateSemesterGpa(Long studentId, Long semesterId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with ID: " + studentId);
        }
        if (!semesterRepository.existsById(semesterId)) {
            throw new ResourceNotFoundException("Semester not found with ID: " + semesterId);
        }

        List<Performance> performances = performanceRepository.findByStudentIdAndSemesterId(studentId, semesterId);
        return computeGpaFromPerformances(performances);
    }

    @Transactional(readOnly = true)
    public Double calculateOverallCgpa(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with ID: " + studentId);
        }

        List<Performance> performances = performanceRepository.findByStudentId(studentId);
        return computeGpaFromPerformances(performances);
    }

    @Transactional(readOnly = true)
    public StudentAcademicSummary getStudentAcademicSummary(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        StudentResponse studentResponse = studentService.mapToResponse(student);
        List<Performance> performances = performanceRepository.findByStudentId(studentId);

        // Convert performance entities to DTO responses using Stream API
        List<PerformanceResponse> allPerformanceResponses = performances.stream()
                .map(performanceService::mapToResponse)
                .collect(Collectors.toList());

        // Calculate total credits using Stream API
        int totalCredits = performances.stream()
                .mapToInt(p -> p.getSubject().getCredits())
                .sum();

        // Calculate overall CGPA
        double cgpa = computeGpaFromPerformances(performances);

        // Determine Academic Status
        String status = AcademicStatus.fromCgpa(cgpa).getDisplayName();

        // Group performance records by Semester using Stream Collectors.groupingBy
        Map<Semester, List<Performance>> semesterGrouped = performances.stream()
                .collect(Collectors.groupingBy(Performance::getSemester));

        // Build Semester-wise GPA Summaries using Java Streams
        List<SemesterGpaSummary> semesterSummaries = semesterGrouped.entrySet().stream()
                .map(entry -> {
                    Semester semester = entry.getKey();
                    List<Performance> semPerformances = entry.getValue();
                    double gpa = computeGpaFromPerformances(semPerformances);
                    int semCredits = semPerformances.stream()
                            .mapToInt(p -> p.getSubject().getCredits())
                            .sum();

                    List<PerformanceResponse> semPerfResponses = semPerformances.stream()
                            .map(performanceService::mapToResponse)
                            .collect(Collectors.toList());

                    return new SemesterGpaSummary(
                            semester.getId(),
                            semester.getSemesterNumber(),
                            semester.getAcademicYear(),
                            gpa,
                            semCredits,
                            semPerfResponses
                    );
                })
                .sorted(Comparator.comparingInt(SemesterGpaSummary::getSemesterNumber))
                .collect(Collectors.toList());

        return new StudentAcademicSummary(
                studentResponse,
                totalCredits,
                cgpa,
                status,
                semesterSummaries,
                allPerformanceResponses
        );
    }

    private double computeGpaFromPerformances(List<Performance> performances) {
        if (performances == null || performances.isEmpty()) {
            return 0.0;
        }

        int totalCredits = performances.stream()
                .mapToInt(p -> p.getSubject().getCredits())
                .sum();

        if (totalCredits == 0) {
            return 0.0;
        }

        // Weighted grade points sum = SUM(Grade Point * Subject Credits)
        double totalWeightedPoints = performances.stream()
                .mapToDouble(p -> p.getGradePoint() * p.getSubject().getCredits())
                .sum();

        double gpa = totalWeightedPoints / totalCredits;

        // Round to 2 decimal places cleanly
        return BigDecimal.valueOf(gpa)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
