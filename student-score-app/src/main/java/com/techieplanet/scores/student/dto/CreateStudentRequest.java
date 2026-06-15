package com.techieplanet.scores.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateStudentRequest(
        @NotBlank(message = "First name is required")
        @Schema(description = "Student's first name", example = "Jane")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Schema(description = "Student's last name", example = "Doe")
        String lastName,

        @Valid
        @NotEmpty(message = "Scores are required")
        @Size(
                min = 5,
                max = 5,
                message = "Exactly 5 subject scores are required"
        )
        @Schema(description = "Scores for exactly five distinct subjects")
        List<ScoreRequest> scores
) {
}
