package com.techieplanet.scores.student.dto;

import java.util.List;

public record StudentResponse(
        Long id,
        String firstName,
        String lastName,
        List<ScoreResponse> scores
) {
}
