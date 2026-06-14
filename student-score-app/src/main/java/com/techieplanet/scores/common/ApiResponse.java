package com.techieplanet.scores.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private boolean success;
    private int status;
    private String message;
    private Object data;
    private Map<String, String> errors;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
