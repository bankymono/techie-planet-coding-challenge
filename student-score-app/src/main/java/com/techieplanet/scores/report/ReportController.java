package com.techieplanet.scores.report;

import com.techieplanet.scores.common.ApiResponse;
import com.techieplanet.scores.common.PageResponse;
import com.techieplanet.scores.report.dto.StudentReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports/students")
@Tag(name = "Reports", description = "APIs for student score reports")
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    @Operation(summary = "Get paginated student score reports")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Student reports retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "Student reports retrieved successfully",
                                      "data": {
                                        "content": [
                                          {
                                            "studentId": 1,
                                            "firstName": "Jane",
                                            "lastName": "Doe",
                                            "scores": [
                                              {"subject": "Biology", "score": 75},
                                              {"subject": "Chemistry", "score": 80},
                                              {"subject": "English", "score": 70},
                                              {"subject": "Mathematics", "score": 80},
                                              {"subject": "Physics", "score": 90}
                                            ],
                                            "mean": 79.00,
                                            "median": 80,
                                            "mode": [80]
                                          }
                                        ],
                                        "page": 0,
                                        "totalElements": 1,
                                        "totalPages": 1
                                      },
                                      "timestamp": "2026-06-15T19:51:07"
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<ApiResponse> getStudentReports(
            @Parameter(description = "Filter by first name or last name", example = "Jane")
            @RequestParam(required = false) String search,
            @ParameterObject
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<StudentReportResponse> reports =
                reportService.getStudentReports(search, pageable);

        PageResponse pageResponse = new PageResponse(
                reports.getContent(),
                reports.getNumber(),
                reports.getTotalElements(),
                reports.getTotalPages()
        );

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Student reports retrieved successfully")
                .data(pageResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studentId}")
    @Operation(summary = "Get one student score report")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Student report retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "Student report retrieved successfully",
                                              "data": {
                                                "studentId": 1,
                                                "firstName": "Jane",
                                                "lastName": "Doe",
                                                "scores": [
                                                  {"subject": "Biology", "score": 75},
                                                  {"subject": "Chemistry", "score": 80},
                                                  {"subject": "English", "score": 70},
                                                  {"subject": "Mathematics", "score": 80},
                                                  {"subject": "Physics", "score": 90}
                                                ],
                                                "mean": 79.00,
                                                "median": 80,
                                                "mode": [80]
                                              },
                                              "timestamp": "2026-06-15T19:51:07"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 404,
                                              "message": "Student not found",
                                              "timestamp": "2026-06-15T19:51:07"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse> getStudentReport(
            @Parameter(description = "Student ID", example = "1")
            @PathVariable Long studentId
    ) {
        StudentReportResponse report =
                reportService.getStudentReport(studentId);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Student report retrieved successfully")
                .data(report)
                .build();

        return ResponseEntity.ok(response);
    }
}
