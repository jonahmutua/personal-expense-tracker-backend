package com.jonah.exception.global;

import com.jonah.dto.ApiResponseDto;
import com.jonah.exception.ResourceNotFoundException;
import com.jonah.exception.UnauthorizedException;
import com.jonah.exception.expense.ExpenseNotFoundException;
import com.jonah.exception.expense.ExpenseStorageException;
import com.jonah.exception.expense.InvalidExpenseDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleExpenseNotFoundException(ExpenseNotFoundException e){
        log.error("Expense not found: {}", e.getMessage());

        ApiResponseDto<Void> response = new ApiResponseDto<>(
                false,
                e.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body( response );
    }

    @ExceptionHandler(InvalidExpenseDataException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleInvalidExpenseData(InvalidExpenseDataException ex) {
        log.error("An invalid field in Expense data: {}", ex.getMessage());

        ApiResponseDto<Void> response = new ApiResponseDto<>(
                false,
                ex.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body( response);
    }

    @ExceptionHandler(ExpenseStorageException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleExpenseStorageException(ExpenseStorageException e) {
        log.error("Storage error: {}", e.getMessage());

        ApiResponseDto<Void> response = new ApiResponseDto<>(
                false,
                e.getMessage(),
                null
        );

        return  ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body( response);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());

        ApiResponseDto<Void> response = new ApiResponseDto<>(
                false,
                ex.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleUnauthorized(UnauthorizedException ex) {
        log.error("Unauthorized: {}", ex.getMessage());

        ApiResponseDto<Void> response = new ApiResponseDto<>(
                false,
                ex.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Invalid argument: {}", ex.getMessage());

        ApiResponseDto<Void> response = new ApiResponseDto<>(
                false,
                "Invalid request: " + ex.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleGeneralException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);

        ApiResponseDto<Void> response = new ApiResponseDto<>(
                false,
                "An unexpected error occurred. Please try again later.",
                null
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
