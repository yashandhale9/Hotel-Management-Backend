//////////////////////////////////////////////////////////////////
// Class Name : GlobalExceptionHandler
// Layer      : Configuration / Exception Handling Layer
// Purpose    : Handles all exceptions globally in Hotel Management.
// Description:
// - Catches custom exceptions like ResourceNotFound, BadRequest,
//   DuplicateResource.
// - Returns proper HTTP status with error message.
// - Prevents application crash and gives user-friendly response.
// - Uses ApiError object to send structured error details.
// Author     : Yash Gorakshnath Andhale
//////////////////////////////////////////////////////////////////
package com.yash.hotelmanagement.config;

import com.yash.hotelmanagement.exception.BadRequestException;
import com.yash.hotelmanagement.exception.DuplicateResourceException;
import com.yash.hotelmanagement.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //////////////////////////////////////////////////////////////////
// Function Name : handleNotFound
// Inputs        : ResourceNotFoundException ex, WebRequest request
// Outputs       : ResponseEntity<Object>
// Description   :
// - Called when resource (Hotel, Room, User, Branch etc.) not found.
// - Logs warning message.
// - Creates ApiError with HTTP 404 status.
// - Returns error response to client.
    //////////////////////////////////////////////////////////////////
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        logger.warn("Resource not found: {}", ex.getMessage());
        String path = request.getDescription(false);
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), path);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    //////////////////////////////////////////////////////////////////
// Function Name : handleBadRequest
// Inputs        : BadRequestException ex, WebRequest request
// Outputs       : ResponseEntity<Object>
// Description   :
// - Called when invalid data is sent by user.
// - Example: Missing required fields like city, phone.
// - Creates ApiError with HTTP 400 status.
// - Returns error message to client.
    //////////////////////////////////////////////////////////////////
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(BadRequestException ex, WebRequest request) {
        logger.info("Bad request: {}", ex.getMessage());
        String path = request.getDescription(false);
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage(), path);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    //////////////////////////////////////////////////////////////////
// Function Name : handleDuplicate
// Inputs        : DuplicateResourceException ex, WebRequest request
// Outputs       : ResponseEntity<Object>
// Description   :
// - Called when duplicate data found.
// - Example: Same email or hotel already exists.
// - Creates ApiError with HTTP 409 status.
// - Returns conflict error message.
    //////////////////////////////////////////////////////////////////
    @ExceptionHandler(DuplicateResourceException.class)
    protected ResponseEntity<Object> handleDuplicate(DuplicateResourceException ex, WebRequest request) {
        logger.info("Duplicate resource: {}", ex.getMessage());
        String path = request.getDescription(false);
        ApiError error = new ApiError(HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage(), path);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    //////////////////////////////////////////////////////////////////
// Function Name : handleAll
// Inputs        : Exception ex, WebRequest request
// Outputs       : ResponseEntity<Object>
// Description   :
// - Handles any unexpected exception.
// - Logs error message.
// - Creates ApiError with HTTP 500 status.
// - Returns internal server error response.
    //////////////////////////////////////////////////////////////////
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        logger.error("Unhandled exception", ex);
        String path = request.getDescription(false);
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", ex.getMessage(), path);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    //////////////////////////////////////////////////////////////////
// Function Name : handleMethodArgumentNotValid
// Inputs        : MethodArgumentNotValidException ex,
//                 HttpHeaders headers,
//                 HttpStatusCode status,
//                 WebRequest request
// Outputs       : ResponseEntity<Object>
// Description   :
// - Handles validation errors from @Valid annotations.
// - Collects all validation messages.
// - Creates ApiError with HTTP 400 status.
// - Returns validation error response.
    //////////////////////////////////////////////////////////////////
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        String msg = ex.getBindingResult().getAllErrors().stream()
                .map(e -> e.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b).orElse(ex.getMessage());
        String path = request.getDescription(false);
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), "Validation Failed", msg, path);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
