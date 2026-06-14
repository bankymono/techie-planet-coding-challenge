package com.techieplanet.scores.student;

import com.techieplanet.scores.exception.InvalidRequestException;
import com.techieplanet.scores.score.StudentScore;
import com.techieplanet.scores.score.StudentScoreRepository;
import com.techieplanet.scores.student.dto.CreateStudentRequest;
import com.techieplanet.scores.student.dto.ScoreRequest;
import com.techieplanet.scores.student.dto.ScoreResponse;
import com.techieplanet.scores.student.dto.StudentResponse;
import com.techieplanet.scores.subject.Subject;
import com.techieplanet.scores.subject.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentService {
    private static final int REQUIRED_SUBJECT_COUNT = 5;

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final StudentScoreRepository studentScoreRepository;

    @Transactional
    public StudentResponse createStudent(CreateStudentRequest request) {
        validateScores(request.scores());

        Student student = studentRepository.save(
                new Student(
                        request.firstName().trim(),
                        request.lastName().trim()
                )
        );

        List<StudentScore> scores = request.scores()
                .stream()
                .map(scoreRequest -> createStudentScore(student, scoreRequest))
                .toList();

        List<StudentScore> savedScores = studentScoreRepository.saveAll(scores);

        List<ScoreResponse> scoreResponses = savedScores.stream()
                .map(score -> new ScoreResponse(
                        score.getSubject().getName(),
                        score.getScore()
                ))
                .toList();

        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                scoreResponses
        );
    }

    private StudentScore createStudentScore(
            Student student,
            ScoreRequest scoreRequest
    ) {
        String subjectName = scoreRequest.subject().trim();

        Subject subject = subjectRepository
                .findByNameIgnoreCase(subjectName)
                .orElseGet(() ->
                        subjectRepository.save(new Subject(subjectName))
                );

        return new StudentScore(
                student,
                subject,
                scoreRequest.score()
        );
    }

    private void validateScores(List<ScoreRequest> scores) {
        if (scores == null || scores.size() != REQUIRED_SUBJECT_COUNT) {
            throw new InvalidRequestException(
                    "Exactly 5 subject scores are required"
            );
        }

        Set<String> subjectNames = new HashSet<>();

        for (ScoreRequest scoreRequest : scores) {
            validateScore(scoreRequest);

            String normalizedSubject = scoreRequest.subject()
                    .trim()
                    .toLowerCase(Locale.ROOT);

            if (!subjectNames.add(normalizedSubject)) {
                throw new InvalidRequestException(
                        "Each subject must appear only once"
                );
            }
        }
    }

    private void validateScore(ScoreRequest scoreRequest) {
        if (scoreRequest == null
                || scoreRequest.subject() == null
                || scoreRequest.subject().isBlank()) {
            throw new InvalidRequestException("Subject is required");
        }

        if (scoreRequest.score() == null
                || scoreRequest.score() < 0
                || scoreRequest.score() > 100) {
            throw new InvalidRequestException(
                    "Score must be between 0 and 100"
            );
        }
    }
}
