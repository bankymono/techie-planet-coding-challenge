package com.techieplanet.scores.report;

import com.techieplanet.scores.exception.ResourceNotFoundException;
import com.techieplanet.scores.report.dto.StudentReportResponse;
import com.techieplanet.scores.score.StudentScore;
import com.techieplanet.scores.score.StudentScoreRepository;
import com.techieplanet.scores.student.Student;
import com.techieplanet.scores.student.StudentRepository;
import com.techieplanet.scores.subject.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentScoreRepository studentScoreRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void shouldReturnStudentReportWithMeanMedianAndMode() {
        Student student = student(1L, "Jane", "Doe");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentScoreRepository.findByStudentId(1L))
                .thenReturn(List.of(
                        score(student, "Mathematics", 80),
                        score(student, "English", 70),
                        score(student, "Physics", 90),
                        score(student, "Chemistry", 80),
                        score(student, "Biology", 75)
                ));

        StudentReportResponse response = reportService.getStudentReport(1L);

        assertEquals(1L, response.studentId());
        assertEquals("Jane", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals(5, response.scores().size());
        assertEquals(new BigDecimal("79.00"), response.mean());
        assertEquals(BigDecimal.valueOf(80), response.median());
        assertEquals(List.of(80), response.mode());
    }

    @Test
    void shouldReturnEmptyModeWhenNoScoreRepeats() {
        Student student = student(1L, "Jane", "Doe");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentScoreRepository.findByStudentId(1L))
                .thenReturn(List.of(
                        score(student, "Mathematics", 80),
                        score(student, "English", 70),
                        score(student, "Physics", 90),
                        score(student, "Chemistry", 85),
                        score(student, "Biology", 75)
                ));

        StudentReportResponse response = reportService.getStudentReport(1L);

        assertTrue(response.mode().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenStudentDoesNotExist() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reportService.getStudentReport(99L)
        );

        assertEquals("Student not found", exception.getMessage());
        verify(studentScoreRepository, never()).findByStudentId(99L);
    }

    @Test
    void shouldReturnPaginatedReportsUsingSearchFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Student student = student(1L, "Jane", "Doe");
        Page<Student> students = new PageImpl<>(List.of(student), pageable, 1);

        when(studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                "jane",
                "jane",
                pageable
        )).thenReturn(students);
        when(studentScoreRepository.findByStudentIdIn(List.of(1L)))
                .thenReturn(List.of(
                        score(student, "Mathematics", 80),
                        score(student, "English", 70),
                        score(student, "Physics", 90),
                        score(student, "Chemistry", 80),
                        score(student, "Biology", 75)
                ));

        Page<StudentReportResponse> response =
                reportService.getStudentReports(" jane ", pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals("Jane", response.getContent().get(0).firstName());

        verify(studentRepository)
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        "jane",
                        "jane",
                        pageable
                );
        verify(studentScoreRepository).findByStudentIdIn(List.of(1L));
    }

    private Student student(Long id, String firstName, String lastName) {
        Student student = new Student(firstName, lastName);
        ReflectionTestUtils.setField(student, "id", id);
        return student;
    }

    private StudentScore score(Student student, String subjectName, Integer score) {
        return new StudentScore(student, subject(subjectName), score);
    }

    private Subject subject(String name) {
        Subject subject = new Subject(name);
        ReflectionTestUtils.setField(subject, "id", name.hashCode() & 0xffffffffL);
        return subject;
    }
}
