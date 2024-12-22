package com.example.acccreation.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Catches any DataIntegrityViolationException that occurs in any controller
     * (e.g., duplicate key constraint).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // Extract the root cause message (or getMostSpecificCause())
        String rootCause = ex.getMostSpecificCause().getMessage();

        // Build a user-friendly error message
        // e.g. "Duplicate entry 'lasinduthemiya96@gmail.com' for key 'admin.UKc0r9atamxvbhjjvy5j8da1kam'"
        String errorMessage = "A data integrity violation occurred: " + rootCause;

        // Return a 400 Bad Request (or 409 Conflict) with the error in the response body
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }
}
