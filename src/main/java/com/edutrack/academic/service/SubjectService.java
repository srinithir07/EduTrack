package com.edutrack.academic.service;

import com.edutrack.academic.dto.SubjectRequest;
import com.edutrack.academic.dto.SubjectResponse;
import com.edutrack.academic.entity.Subject;
import com.edutrack.academic.exception.BusinessRuleException;
import com.edutrack.academic.exception.ResourceNotFoundException;
import com.edutrack.academic.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Transactional(readOnly = true)
    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubjectResponse getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));
        return mapToResponse(subject);
    }

    @Transactional
    public SubjectResponse createSubject(SubjectRequest request) {
        if (subjectRepository.existsBySubjectCode(request.getSubjectCode())) {
            throw new BusinessRuleException("Subject with code " + request.getSubjectCode() + " already exists");
        }

        Subject subject = new Subject(
                request.getSubjectCode(),
                request.getSubjectName(),
                request.getCredits()
        );

        Subject savedSubject = subjectRepository.save(subject);
        return mapToResponse(savedSubject);
    }

    @Transactional
    public SubjectResponse updateSubject(Long id, SubjectRequest request) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));

        if (!subject.getSubjectCode().equalsIgnoreCase(request.getSubjectCode()) &&
                subjectRepository.existsBySubjectCode(request.getSubjectCode())) {
            throw new BusinessRuleException("Subject with code " + request.getSubjectCode() + " already exists");
        }

        subject.setSubjectCode(request.getSubjectCode());
        subject.setSubjectName(request.getSubjectName());
        subject.setCredits(request.getCredits());

        Subject updatedSubject = subjectRepository.save(subject);
        return mapToResponse(updatedSubject);
    }

    @Transactional
    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject not found with ID: " + id);
        }
        subjectRepository.deleteById(id);
    }

    public SubjectResponse mapToResponse(Subject subject) {
        return new SubjectResponse(
                subject.getId(),
                subject.getSubjectCode(),
                subject.getSubjectName(),
                subject.getCredits()
        );
    }
}
