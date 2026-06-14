package com.techieplanet.scores.score;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StudentScoreRepository extends JpaRepository<StudentScore, Long> {
    List<StudentScore> findByStudentId(Long studentId);

    List<StudentScore> findByStudentIdIn(Collection<Long> studentIds);
}
