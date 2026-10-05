package com.edutrack.academic.controller;

import com.edutrack.academic.dto.SemesterRequest;
import com.edutrack.academic.dto.SemesterResponse;
import com.edutrack.academic.service.SemesterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
public class SemesterController {

    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @GetMapping
    public ResponseEntity<List<SemesterResponse>> getAllSemesters() {
        List<SemesterResponse> semesters = semesterService.getAllSemesters();
        return ResponseEntity.ok(semesters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SemesterResponse> getSemesterById(@PathVariable Long id) {
        SemesterResponse semester = semesterService.getSemesterById(id);
        return ResponseEntity.ok(semester);
    }

    @PostMapping
    public ResponseEntity<SemesterResponse> createSemester(@Valid @RequestBody SemesterRequest request) {
        SemesterResponse createdSemester = semesterService.createSemester(request);
        return new ResponseEntity<>(createdSemester, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SemesterResponse> updateSemester(@PathVariable Long id,
                                                          @Valid @RequestBody SemesterRequest request) {
        SemesterResponse updatedSemester = semesterService.updateSemester(id, request);
        return ResponseEntity.ok(updatedSemester);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return ResponseEntity.noContent().build();
    }
}
