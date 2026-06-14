package com.techieplanet.scores.student;

import com.techieplanet.scores.exception.InvalidRequestException;
import com.techieplanet.scores.score.StudentScoreRepository;
import com.techieplanet.scores.student.dto.CreateStudentRequest;
import com.techieplanet.scores.student.dto.ScoreRequest;
import com.techieplanet.scores.student.dto.StudentResponse;
import com.techieplanet.scores.subject.Subject;
import com.techieplanet.scores.subject.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private StudentScoreRepository studentScoreRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void shouldCreateStudentWithFiveScores() {
        CreateStudentRequest request = new CreateStudentRequest(
                " Jane ",
                " Doe ",
                validScores()
        );

        when(studentRepository.save(any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(subjectRepository.findByNameIgnoreCase(anyString()))
                .thenReturn(Optional.empty());
        when(subjectRepository.save(any(Subject.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(studentScoreRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StudentResponse response = studentService.createStudent(request);

        assertEquals("Jane", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals(5, response.scores().size());

        verify(studentRepository).save(any(Student.class));
        verify(studentScoreRepository).saveAll(anyList());
    }

    @Test
    void shouldRejectRequestWithoutExactlyFiveScores() {
        CreateStudentRequest request = new CreateStudentRequest(
                "Jane",
                "Doe",
                validScores().subList(0, 4)
        );

        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> studentService.createStudent(request)
        );

        assertEquals(
                "Exactly 5 subject scores are required",
                exception.getMessage()
        );

        verifyNoInteractions(
                studentRepository,
                subjectRepository,
                studentScoreRepository
        );
    }

    @Test
    void shouldRejectDuplicateSubjectsIgnoringCase() {
        List<ScoreRequest> scores = List.of(
                new ScoreRequest("Mathematics", 80),
                new ScoreRequest("mathematics", 70),
                new ScoreRequest("Physics", 90),
                new ScoreRequest("Chemistry", 80),
                new ScoreRequest("Biology", 75)
        );

        CreateStudentRequest request =
                new CreateStudentRequest("Jane", "Doe", scores);

        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> studentService.createStudent(request)
        );

        assertEquals(
                "Each subject must appear only once",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectScoreOutsideAllowedRange() {
        List<ScoreRequest> scores = List.of(
                new ScoreRequest("Mathematics", 101),
                new ScoreRequest("English", 70),
                new ScoreRequest("Physics", 90),
                new ScoreRequest("Chemistry", 80),
                new ScoreRequest("Biology", 75)
        );

        CreateStudentRequest request =
                new CreateStudentRequest("Jane", "Doe", scores);

        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> studentService.createStudent(request)
        );

        assertEquals(
                "Score must be between 0 and 100",
                exception.getMessage()
        );
    }

    @Test
    void shouldReuseExistingSubject() {
        Subject mathematics = new Subject("Mathematics");

        when(studentRepository.save(any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(subjectRepository.findByNameIgnoreCase(anyString()))
                .thenAnswer(invocation -> {
                    String subjectName = invocation.getArgument(0);
                    return "Mathematics".equals(subjectName)
                            ? Optional.of(mathematics)
                            : Optional.empty();
                });
        when(subjectRepository.save(any(Subject.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(studentScoreRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        studentService.createStudent(
                new CreateStudentRequest("Jane", "Doe", validScores())
        );

        verify(subjectRepository, never()).save(mathematics);
    }

    private List<ScoreRequest> validScores() {
        return List.of(
                new ScoreRequest("Mathematics", 80),
                new ScoreRequest("English", 70),
                new ScoreRequest("Physics", 90),
                new ScoreRequest("Chemistry", 80),
                new ScoreRequest("Biology", 75)
        );
    }
}
