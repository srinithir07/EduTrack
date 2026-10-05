package com.edutrack.academic;

import com.edutrack.academic.dto.StudentRequest;
import com.edutrack.academic.dto.StudentResponse;
import com.edutrack.academic.entity.Student;
import com.edutrack.academic.exception.BusinessRuleException;
import com.edutrack.academic.exception.ResourceNotFoundException;
import com.edutrack.academic.repository.StudentRepository;
import com.edutrack.academic.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentRequest studentRequest;

    @BeforeEach
    void setUp() {
        student = new Student(1L, "21CS101", "Aarav Sharma", "aarav@example.com", "Computer Science", "2021-2025");
        studentRequest = new StudentRequest("21CS101", "Aarav Sharma", "aarav@example.com", "Computer Science", "2021-2025");
    }

    @Test
    @DisplayName("Should successfully create student when register number is unique")
    void testCreateStudentSuccess() {
        when(studentRepository.existsByRegisterNumber("21CS101")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponse response = studentService.createStudent(studentRequest);

        assertNotNull(response);
        assertEquals("21CS101", response.getRegisterNumber());
        assertEquals("Aarav Sharma", response.getName());
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when register number already exists")
    void testCreateStudentDuplicateRegisterNumber() {
        when(studentRepository.existsByRegisterNumber("21CS101")).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> studentService.createStudent(studentRequest));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student ID does not exist")
    void testGetStudentByIdNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(99L));
    }
}
