package com.edutrack.academic.controller;

import com.edutrack.academic.dto.StudentAcademicSummary;
import com.edutrack.academic.service.AcademicCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/academic")
public class AcademicController {

    private final AcademicCalculationService academicCalculationService;

    public AcademicController(AcademicCalculationService academicCalculationService) {
        this.academicCalculationService = academicCalculationService;
    }

    @GetMapping("/students/{studentId}/summary")
    public ResponseEntity<StudentAcademicSummary> getStudentSummary(@PathVariable Long studentId) {
        StudentAcademicSummary summary = academicCalculationService.getStudentAcademicSummary(studentId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/students/{studentId}/cgpa")
    public ResponseEntity<Map<String, Object>> getStudentCgpa(@PathVariable Long studentId) {
        Double cgpa = academicCalculationService.calculateOverallCgpa(studentId);
        return ResponseEntity.ok(Map.of(
                "studentId", studentId,
                "cgpa", cgpa
        ));
    }

    @GetMapping("/students/{studentId}/semesters/{semesterId}/gpa")
    public ResponseEntity<Map<String, Object>> getSemesterGpa(@PathVariable Long studentId,
                                                               @PathVariable Long semesterId) {
        Double gpa = academicCalculationService.calculateSemesterGpa(studentId, semesterId);
        return ResponseEntity.ok(Map.of(
                "studentId", studentId,
                "semesterId", semesterId,
                "gpa", gpa
        ));
    }
}
