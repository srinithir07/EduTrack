package com.edutrack.academic.repository;

import com.edutrack.academic.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    List<Performance> findByStudentId(Long studentId);

    List<Performance> findByStudentIdAndSemesterId(Long studentId, Long semesterId);

    Optional<Performance> findByStudentIdAndSubjectIdAndSemesterId(Long studentId, Long subjectId, Long semesterId);

    boolean existsByStudentIdAndSubjectIdAndSemesterId(Long studentId, Long subjectId, Long semesterId);
}
