package com.marketplace.backend_v2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        Boolean success,
        String message,
        T data,
        LocalDateTime timestamp,
        String path
) {
    public ApiResponse {
        if (success == null) success = true;
        if (timestamp == null) timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Operación exitosa")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    public ApiResponse<T> withPath(String path) {
        return ApiResponse.<T>builder()
                .success(this.success)
                .message(this.message)
                .data(this.data)
                .timestamp(this.timestamp)
                .path(path)
                .build();
    }
}