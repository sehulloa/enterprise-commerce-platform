package com.company.platform.common.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "Standard successful API response")
@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @Schema(description = "Indicates whether the request was successful", example = "true")
    private final boolean success;

    @Schema(description = "Response message", example = "Order created successfully")

    private final String message;

    @Schema(description = "Response data payload")
    private final T data;

    @Schema(description = "Response timestamp", example = "2026-04-01T22:15:30")
    private final LocalDateTime timestamp;

    @Schema(description = "Trace identifier for debugging", example = "79c331d1-1fa4-45c0-b33c-75dde2e1e28d")
    private final String traceId;

}
