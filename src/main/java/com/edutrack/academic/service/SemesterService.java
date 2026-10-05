package com.edutrack.academic.service;

import com.edutrack.academic.dto.SemesterRequest;
import com.edutrack.academic.dto.SemesterResponse;
import com.edutrack.academic.entity.Semester;
import com.edutrack.academic.exception.BusinessRuleException;
import com.edutrack.academic.exception.ResourceNotFoundException;
import com.edutrack.academic.repository.SemesterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public SemesterService(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    @Transactional(readOnly = true)
    public List<SemesterResponse> getAllSemesters() {
        return semesterRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SemesterResponse getSemesterById(Long id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found with ID: " + id));
        return mapToResponse(semester);
    }

    @Transactional
    public SemesterResponse createSemester(SemesterRequest request) {
        if (semesterRepository.existsBySemesterNumberAndAcademicYear(request.getSemesterNumber(), request.getAcademicYear())) {
            throw new BusinessRuleException("Semester " + request.getSemesterNumber() + " for academic year " + request.getAcademicYear() + " already exists");
        }

        Semester semester = new Semester(
                request.getSemesterNumber(),
                request.getAcademicYear()
        );

        Semester savedSemester = semesterRepository.save(semester);
        return mapToResponse(savedSemester);
    }

    @Transactional
    public SemesterResponse updateSemester(Long id, SemesterRequest request) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found with ID: " + id));

        boolean isDifferent = !semester.getSemesterNumber().equals(request.getSemesterNumber()) ||
                !semester.getAcademicYear().equalsIgnoreCase(request.getAcademicYear());

        if (isDifferent && semesterRepository.existsBySemesterNumberAndAcademicYear(request.getSemesterNumber(), request.getAcademicYear())) {
            throw new BusinessRuleException("Semester " + request.getSemesterNumber() + " for academic year " + request.getAcademicYear() + " already exists");
        }

        semester.setSemesterNumber(request.getSemesterNumber());
        semester.setAcademicYear(request.getAcademicYear());

        Semester updatedSemester = semesterRepository.save(semester);
        return mapToResponse(updatedSemester);
    }

    @Transactional
    public void deleteSemester(Long id) {
        if (!semesterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Semester not found with ID: " + id);
        }
        semesterRepository.deleteById(id);
    }

    public SemesterResponse mapToResponse(Semester semester) {
        return new SemesterResponse(
                semester.getId(),
                semester.getSemesterNumber(),
                semester.getAcademicYear()
        );
    }
}
