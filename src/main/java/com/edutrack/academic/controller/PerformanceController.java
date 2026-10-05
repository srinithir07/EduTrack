package com.edutrack.academic.controller;

import com.edutrack.academic.dto.PerformanceRequest;
import com.edutrack.academic.dto.PerformanceResponse;
import com.edutrack.academic.service.PerformanceService;
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
@RequestMapping("/api/performance")
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping
    public ResponseEntity<List<PerformanceResponse>> getAllPerformances() {
        List<PerformanceResponse> performances = performanceService.getAllPerformances();
        return ResponseEntity.ok(performances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerformanceResponse> getPerformanceById(@PathVariable Long id) {
        PerformanceResponse performance = performanceService.getPerformanceById(id);
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<PerformanceResponse>> getPerformancesByStudentId(@PathVariable Long studentId) {
        List<PerformanceResponse> performances = performanceService.getPerformancesByStudentId(studentId);
        return ResponseEntity.ok(performances);
    }

    @PostMapping
    public ResponseEntity<PerformanceResponse> createPerformance(@Valid @RequestBody PerformanceRequest request) {
        PerformanceResponse created = performanceService.createPerformance(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerformanceResponse> updatePerformance(@PathVariable Long id,
                                                                 @Valid @RequestBody PerformanceRequest request) {
        PerformanceResponse updated = performanceService.updatePerformance(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long id) {
        performanceService.deletePerformance(id);
        return ResponseEntity.noContent().build();
    }
}
