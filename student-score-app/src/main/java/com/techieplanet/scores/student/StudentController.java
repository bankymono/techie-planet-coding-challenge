package com.techieplanet.scores.student;

import com.techieplanet.scores.common.ApiResponse;
import com.techieplanet.scores.student.dto.CreateStudentRequest;
import com.techieplanet.scores.student.dto.StudentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Student scores created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 201,
                                              "message": "Student scores created successfully",
                                              "data": {
                                                "id": 1,
                                                "firstName": "Jane",
                                                "lastName": "Doe",
                                                "scores": [
                                                  {"subject": "Mathematics", "score": 80},
                                                  {"subject": "English", "score": 70},
                                                  {"subject": "Physics", "score": 90},
                                                  {"subject": "Chemistry", "score": 80},
                                                  {"subject": "Biology", "score": 75}
                                                ]
                                              },
                                              "timestamp": "2026-06-15T19:51:07"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Request validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 400,
                                              "message": "Validation failed",
                                              "errors": {
                                                "scores[0].score": "Score must not be greater than 100"
                                              },
                                              "timestamp": "2026-06-15T19:51:07"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse> createStudent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Student details and scores for exactly five distinct subjects",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateStudentRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "firstName": "Jane",
                                              "lastName": "Doe",
                                              "scores": [
                                                {"subject": "Mathematics", "score": 80},
                                                {"subject": "English", "score": 70},
                                                {"subject": "Physics", "score": 90},
                                                {"subject": "Chemistry", "score": 80},
                                                {"subject": "Biology", "score": 75}
                                              ]
                                            }
                                            """
                            )
                    )
            )
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
