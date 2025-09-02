package com.jonah.exception.global;

import com.jonah.exception.expense.ExpenseNotFoundException;
import com.jonah.exception.expense.ExpenseStorageException;
import com.jonah.exception.expense.InvalidExpenseDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<String> handleExpenseNotFoundException(ExpenseNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidExpenseDataException.class)
    public ResponseEntity<String> handleInvalidExpenseData(InvalidExpenseDataException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpenseStorageException.class)
    public ResponseEntity<String> handleExpenseStorageException(ExpenseStorageException e) {
        return new ResponseEntity<>("Storage error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // handle uncaught exceptions(fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
