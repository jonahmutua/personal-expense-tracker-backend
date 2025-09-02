package com.jonah.exception.expense;

public class InvalidExpenseDataException extends RuntimeException{
    public InvalidExpenseDataException() {
        super("Invalid expense data");
    }
    public InvalidExpenseDataException(String message) {
        super(message);
    }
}
