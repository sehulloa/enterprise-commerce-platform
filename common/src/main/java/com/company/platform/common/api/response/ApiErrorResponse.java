package com.company.platform.common.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "Standard error API response")
@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    @Schema(description = "Indicates whether the request was successful", example = "false")
    private final boolean success;

    @Schema(description = "High-level error message", example = "Validation error")
    private final String message;

    @Schema(description = "Detailed error information", example = "customerId: must not be null")
    private final String error;

    @Schema(description = "Response timestamp", example = "2026-04-01T22:15:30")
    private final LocalDateTime timestamp;

    @Schema(description = "Trace identifier for debugging", example = "79c331d1-1fa4-45c0-b33c-75dde2e1e28d")
    private final String traceId;

}
