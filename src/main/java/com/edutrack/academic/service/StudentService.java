package com.edutrack.academic.service;

import com.edutrack.academic.dto.StudentRequest;
import com.edutrack.academic.dto.StudentResponse;
import com.edutrack.academic.entity.Student;
import com.edutrack.academic.exception.BusinessRuleException;
import com.edutrack.academic.exception.ResourceNotFoundException;
import com.edutrack.academic.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return mapToResponse(student);
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByRegisterNumber(request.getRegisterNumber())) {
            throw new BusinessRuleException("Student with register number " + request.getRegisterNumber() + " already exists");
        }

        Student student = new Student(
                request.getRegisterNumber(),
                request.getName(),
                request.getEmail(),
                request.getDepartment(),
                request.getBatch()
        );

        Student savedStudent = studentRepository.save(student);
        return mapToResponse(savedStudent);
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        // Check register number uniqueness if modified
        if (!student.getRegisterNumber().equalsIgnoreCase(request.getRegisterNumber()) &&
                studentRepository.existsByRegisterNumber(request.getRegisterNumber())) {
            throw new BusinessRuleException("Student with register number " + request.getRegisterNumber() + " already exists");
        }

        student.setRegisterNumber(request.getRegisterNumber());
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setDepartment(request.getDepartment());
        student.setBatch(request.getBatch());

        Student updatedStudent = studentRepository.save(student);
        return mapToResponse(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    public StudentResponse mapToResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getRegisterNumber(),
                student.getName(),
                student.getEmail(),
                student.getDepartment(),
                student.getBatch()
        );
    }
}
