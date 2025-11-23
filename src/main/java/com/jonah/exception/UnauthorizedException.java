package com.jonah.exception;

/**
 * Exception thrown when user is not authorized to access a resource
 */
public class UnauthorizedException extends  RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public static UnauthorizedException accessDenied(String resourceName) {
        return new UnauthorizedException("You do not have permission to access " + resourceName);
    }

    public static UnauthorizedException accessDenied(String resourceName, Long id) {
        return new UnauthorizedException("You do not have permission to access " + resourceName + " with id: " + id);
    }
}
