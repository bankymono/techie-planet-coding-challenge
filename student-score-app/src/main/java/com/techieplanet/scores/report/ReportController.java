package com.techieplanet.scores.report;

import com.techieplanet.scores.common.ApiResponse;
import com.techieplanet.scores.report.dto.StudentReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse> getStudentReports(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<StudentReportResponse> reports =
                reportService.getStudentReports(search, pageable);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Student reports retrieved successfully")
                .data(reports)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studentId}")
    @Operation(summary = "Get one student score report")
    public ResponseEntity<ApiResponse> getStudentReport(
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
