package com.company.platform.common.api.response;

import org.slf4j.MDC;

import java.time.LocalDateTime;

public final class ApiResponseFactory {

    private static final String TRACE_ID_KEY = "traceId";

    private ApiResponseFactory() {
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .traceId(MDC.get(TRACE_ID_KEY))
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(data, null);
    }

    public static ApiResponse<Void> successMessage(String message) {
        return ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .traceId(MDC.get(TRACE_ID_KEY))
                .build();
    }

    public static ApiResponse<Void> error(String errorMessage, String message) {
        return ApiResponse.<Void>builder()
                .success(false)
                .message(message)
                .error(errorMessage)
                .timestamp(LocalDateTime.now())
                .traceId(MDC.get(TRACE_ID_KEY))
                .build();
    }

    public static ApiResponse<Void> error(String errorMessage) {
        return error(errorMessage, null);
    }
}
