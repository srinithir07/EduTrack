package com.edutrack.academic.repository;

import com.edutrack.academic.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

    Optional<Semester> findBySemesterNumberAndAcademicYear(Integer semesterNumber, String academicYear);

    boolean existsBySemesterNumberAndAcademicYear(Integer semesterNumber, String academicYear);
}
