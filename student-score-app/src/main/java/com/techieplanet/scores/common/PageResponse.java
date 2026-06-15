package com.techieplanet.scores.common;

import java.util.List;

public record PageResponse(
        List<?> content,
        int page,
        long totalElements,
        int totalPages
) {
}
