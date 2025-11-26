package com.jonah.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Generice API Respose DTO with URI location
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T> {
    private boolean success;
    private String message;
    private T data;
    private String location; // URI location of the resource

    public ApiResponseDto(boolean success, String message, T data) {
        this(success, message, data, null);
    }

}
