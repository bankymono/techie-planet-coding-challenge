package com.techieplanet.scores.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ScoreRequest(
        @NotBlank(message = "Subject is required")
        @Schema(description = "Subject name", example = "Mathematics")
        String subject,

        @NotNull(message = "Score is required")
        @Min(value = 0, message = "Score must be at least 0")
        @Max(value = 100, message = "Score must not be greater than 100")
        @Schema(description = "Subject score between 0 and 100", example = "80")
        Integer score
) {
}
