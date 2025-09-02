package com.jonah.exception.expense;

public class ExpenseStorageException extends  RuntimeException{
    public ExpenseStorageException(Throwable cause) {
        super("failed to write/read from file", cause);
    }
    public ExpenseStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
