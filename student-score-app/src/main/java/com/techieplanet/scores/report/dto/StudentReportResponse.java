package com.techieplanet.scores.report.dto;

import com.techieplanet.scores.student.dto.ScoreResponse;

import java.math.BigDecimal;
import java.util.List;

public record StudentReportResponse(
        Long studentId,
        String firstName,
        String lastName,
        List<ScoreResponse> scores,
        BigDecimal mean,
        BigDecimal median,
        List<Integer> mode
) {
}
