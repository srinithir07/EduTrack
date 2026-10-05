package com.edutrack.academic.config;

import com.edutrack.academic.dto.PerformanceRequest;
import com.edutrack.academic.entity.Semester;
import com.edutrack.academic.entity.Student;
import com.edutrack.academic.entity.Subject;
import com.edutrack.academic.repository.SemesterRepository;
import com.edutrack.academic.repository.StudentRepository;
import com.edutrack.academic.repository.SubjectRepository;
import com.edutrack.academic.service.PerformanceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final SemesterRepository semesterRepository;
    private final PerformanceService performanceService;

    public DataInitializer(StudentRepository studentRepository,
                           SubjectRepository subjectRepository,
                           SemesterRepository semesterRepository,
                           PerformanceService performanceService) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.semesterRepository = semesterRepository;
        this.performanceService = performanceService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Prevent duplicate insertion every time application starts
        if (studentRepository.count() > 0) {
            return;
        }

        // 1. Seed Students
        Student s1 = studentRepository.save(new Student("21CS101", "Aarav Sharma", "aarav.sharma@example.com", "Computer Science", "2021-2025"));
        Student s2 = studentRepository.save(new Student("21CS102", "Priya Patel", "priya.patel@example.com", "Computer Science", "2021-2025"));
        Student s3 = studentRepository.save(new Student("21EC101", "Rohan Verma", "rohan.verma@example.com", "Electronics & Communication", "2021-2025"));

        // 2. Seed Subjects
        Subject sub1 = subjectRepository.save(new Subject("CS301", "Data Structures & Algorithms", 4));
        Subject sub2 = subjectRepository.save(new Subject("CS302", "Database Management Systems", 4));
        Subject sub3 = subjectRepository.save(new Subject("CS303", "Computer Networks", 3));
        Subject sub4 = subjectRepository.save(new Subject("CS304", "Operating Systems", 3));
        Subject sub5 = subjectRepository.save(new Subject("CS305", "Web Technologies", 3));

        // 3. Seed Semesters
        Semester sem1 = semesterRepository.save(new Semester(1, "2024-2025"));
        Semester sem2 = semesterRepository.save(new Semester(2, "2024-2025"));

        // 4. Seed Performance Records (calculates Grade and Grade Point via backend service)
        // Aarav Sharma - Semester 1
        performanceService.createPerformance(new PerformanceRequest(s1.getId(), sub1.getId(), sem1.getId(), 92.0)); // O (10)
        performanceService.createPerformance(new PerformanceRequest(s1.getId(), sub2.getId(), sem1.getId(), 85.0)); // A+ (9)
        performanceService.createPerformance(new PerformanceRequest(s1.getId(), sub3.getId(), sem1.getId(), 78.0)); // A (8)

        // Aarav Sharma - Semester 2
        performanceService.createPerformance(new PerformanceRequest(s1.getId(), sub4.getId(), sem2.getId(), 88.0)); // A+ (9)
        performanceService.createPerformance(new PerformanceRequest(s1.getId(), sub5.getId(), sem2.getId(), 94.0)); // O (10)

        // Priya Patel - Semester 1
        performanceService.createPerformance(new PerformanceRequest(s2.getId(), sub1.getId(), sem1.getId(), 84.0)); // A+ (9)
        performanceService.createPerformance(new PerformanceRequest(s2.getId(), sub2.getId(), sem1.getId(), 76.0)); // A (8)
        performanceService.createPerformance(new PerformanceRequest(s2.getId(), sub3.getId(), sem1.getId(), 82.0)); // A+ (9)

        // Rohan Verma - Semester 1
        performanceService.createPerformance(new PerformanceRequest(s3.getId(), sub1.getId(), sem1.getId(), 65.0)); // B+ (7)
        performanceService.createPerformance(new PerformanceRequest(s3.getId(), sub2.getId(), sem1.getId(), 72.0)); // A (8)
    }
}
