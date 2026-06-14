package com.techieplanet.scores.student.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateStudentRequest(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @Valid
        @NotEmpty(message = "Scores are required")
        List<ScoreRequest> scores
) {
}
