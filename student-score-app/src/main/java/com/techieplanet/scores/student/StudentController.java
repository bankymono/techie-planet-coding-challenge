package com.techieplanet.scores.student;

import com.techieplanet.scores.common.ApiResponse;
import com.techieplanet.scores.student.dto.CreateStudentRequest;
import com.techieplanet.scores.student.dto.StudentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
@Tag(name = "Students", description = "APIs for managing students and their scores")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Create a student with scores in five subjects")
    public ResponseEntity<ApiResponse> createStudent(
            @Valid @RequestBody CreateStudentRequest request
    ) {
        StudentResponse student = studentService.createStudent(request);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Student scores created successfully")
                .data(student)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
