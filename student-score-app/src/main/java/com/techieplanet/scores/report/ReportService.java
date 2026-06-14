package com.techieplanet.scores.report;

import com.techieplanet.scores.exception.ResourceNotFoundException;
import com.techieplanet.scores.report.dto.StudentReportResponse;
import com.techieplanet.scores.score.StudentScore;
import com.techieplanet.scores.score.StudentScoreRepository;
import com.techieplanet.scores.student.Student;
import com.techieplanet.scores.student.StudentRepository;
import com.techieplanet.scores.student.dto.ScoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final StudentRepository studentRepository;
    private final StudentScoreRepository studentScoreRepository;

    @Transactional(readOnly = true)
    public Page<StudentReportResponse> getStudentReports(
            String search,
            Pageable pageable
    ) {
        Page<Student> students = hasSearch(search)
                ? studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        search.trim(),
                        search.trim(),
                        pageable
                )
                : studentRepository.findAll(pageable);

        List<Long> studentIds = students.getContent()
                .stream()
                .map(Student::getId)
                .toList();

        Map<Long, List<StudentScore>> scoresByStudent =
                getScoresByStudent(studentIds);

        return students.map(student ->
                buildReport(
                        student,
                        scoresByStudent.getOrDefault(student.getId(), List.of())
                )
        );
    }

    @Transactional(readOnly = true)
    public StudentReportResponse getStudentReport(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found")
                );

        List<StudentScore> scores = studentScoreRepository.findByStudentId(studentId);

        return buildReport(student, scores);
    }

    private Map<Long, List<StudentScore>> getScoresByStudent(
            List<Long> studentIds
    ) {
        if (studentIds.isEmpty()) {
            return Map.of();
        }

        return studentScoreRepository.findByStudentIdIn(studentIds)
                .stream()
                .collect(Collectors.groupingBy(
                        score -> score.getStudent().getId()
                ));
    }

    private StudentReportResponse buildReport(
            Student student,
            List<StudentScore> scores
    ) {
        List<Integer> scoreValues = scores.stream()
                .map(StudentScore::getScore)
                .sorted()
                .toList();

        List<ScoreResponse> scoreResponses = scores.stream()
                .sorted(Comparator.comparing(score -> score.getSubject().getName()))
                .map(score -> new ScoreResponse(
                        score.getSubject().getName(),
                        score.getScore()
                ))
                .toList();

        return new StudentReportResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                scoreResponses,
                calculateMean(scoreValues),
                calculateMedian(scoreValues),
                calculateMode(scoreValues)
        );
    }

    private BigDecimal calculateMean(List<Integer> scores) {
        if (scores.isEmpty()) {
            return BigDecimal.ZERO;
        }

        int total = scores.stream()
                .mapToInt(Integer::intValue)
                .sum();

        return BigDecimal.valueOf(total)
                .divide(
                        BigDecimal.valueOf(scores.size()),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private BigDecimal calculateMedian(List<Integer> scores) {
        if (scores.isEmpty()) {
            return BigDecimal.ZERO;
        }

        int middle = scores.size() / 2;

        if (scores.size() % 2 == 1) {
            return BigDecimal.valueOf(scores.get(middle));
        }

        int middleTotal = scores.get(middle - 1) + scores.get(middle);

        return BigDecimal.valueOf(middleTotal)
                .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
    }

    private List<Integer> calculateMode(List<Integer> scores) {
        Map<Integer, Integer> frequencies = new HashMap<>();

        for (Integer score : scores) {
            frequencies.put(score, frequencies.getOrDefault(score, 0) + 1);
        }

        int highestFrequency = frequencies.values()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        if (highestFrequency <= 1) {
            return List.of();
        }

        return frequencies.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == highestFrequency)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
    }

    private boolean hasSearch(String search) {
        return search != null && !search.isBlank();
    }
}
