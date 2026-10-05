package com.edutrack.academic.service;

import com.edutrack.academic.dto.PerformanceRequest;
import com.edutrack.academic.dto.PerformanceResponse;
import com.edutrack.academic.entity.Performance;
import com.edutrack.academic.entity.Semester;
import com.edutrack.academic.entity.Student;
import com.edutrack.academic.entity.Subject;
import com.edutrack.academic.enums.Grade;
import com.edutrack.academic.exception.BusinessRuleException;
import com.edutrack.academic.exception.ResourceNotFoundException;
import com.edutrack.academic.repository.PerformanceRepository;
import com.edutrack.academic.repository.SemesterRepository;
import com.edutrack.academic.repository.StudentRepository;
import com.edutrack.academic.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final SemesterRepository semesterRepository;

    public PerformanceService(PerformanceRepository performanceRepository,
                              StudentRepository studentRepository,
                              SubjectRepository subjectRepository,
                              SemesterRepository semesterRepository) {
        this.performanceRepository = performanceRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.semesterRepository = semesterRepository;
    }

    @Transactional(readOnly = true)
    public List<PerformanceResponse> getAllPerformances() {
        return performanceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PerformanceResponse getPerformanceById(Long id) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance record not found with ID: " + id));
        return mapToResponse(performance);
    }

    @Transactional(readOnly = true)
    public List<PerformanceResponse> getPerformancesByStudentId(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with ID: " + studentId);
        }
        return performanceRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PerformanceResponse createPerformance(PerformanceRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + request.getSubjectId()));

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found with ID: " + request.getSemesterId()));

        // Check duplicate record for same student, subject, semester
        Optional<Performance> existing = performanceRepository.findByStudentIdAndSubjectIdAndSemesterId(
                request.getStudentId(), request.getSubjectId(), request.getSemesterId());

        if (existing.isPresent()) {
            throw new BusinessRuleException("Performance record already exists for Student: " + student.getName() +
                    ", Subject: " + subject.getSubjectCode() + ", Semester: " + semester.getSemesterNumber());
        }

        // Calculate Grade and Grade Point in Service Layer
        Grade gradeEnum = Grade.calculateGrade(request.getMarks());

        Performance performance = new Performance(
                student,
                subject,
                semester,
                request.getMarks(),
                gradeEnum.getCode(),
                gradeEnum.getGradePoint()
        );

        Performance saved = performanceRepository.save(performance);
        return mapToResponse(saved);
    }

    @Transactional
    public PerformanceResponse updatePerformance(Long id, PerformanceRequest request) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance record not found with ID: " + id));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + request.getSubjectId()));

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found with ID: " + request.getSemesterId()));

        Grade gradeEnum = Grade.calculateGrade(request.getMarks());

        performance.setStudent(student);
        performance.setSubject(subject);
        performance.setSemester(semester);
        performance.setMarks(request.getMarks());
        performance.setGrade(gradeEnum.getCode());
        performance.setGradePoint(gradeEnum.getGradePoint());

        Performance updated = performanceRepository.save(performance);
        return mapToResponse(updated);
    }

    @Transactional
    public void deletePerformance(Long id) {
        if (!performanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Performance record not found with ID: " + id);
        }
        performanceRepository.deleteById(id);
    }

    public PerformanceResponse mapToResponse(Performance performance) {
        return new PerformanceResponse(
                performance.getId(),
                performance.getStudent().getId(),
                performance.getStudent().getRegisterNumber(),
                performance.getStudent().getName(),
                performance.getSubject().getId(),
                performance.getSubject().getSubjectCode(),
                performance.getSubject().getSubjectName(),
                performance.getSubject().getCredits(),
                performance.getSemester().getId(),
                performance.getSemester().getSemesterNumber(),
                performance.getSemester().getAcademicYear(),
                performance.getMarks(),
                performance.getGrade(),
                performance.getGradePoint()
        );
    }
}
