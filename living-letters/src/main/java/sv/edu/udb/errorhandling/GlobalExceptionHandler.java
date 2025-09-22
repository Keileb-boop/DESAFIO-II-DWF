package sv.edu.udb.errorhandling;

                import jakarta.servlet.http.HttpServletRequest;
                import jakarta.validation.ConstraintViolationException;
                import org.springframework.dao.DataIntegrityViolationException;
                import org.springframework.http.HttpStatus;
                import org.springframework.http.ResponseEntity;
                import org.springframework.http.converter.HttpMessageNotReadableException;
                import org.springframework.web.bind.MethodArgumentNotValidException;
                import org.springframework.web.bind.annotation.ControllerAdvice;
                import org.springframework.web.bind.annotation.ExceptionHandler;

                import java.util.List;
                import java.util.Optional;
                import java.util.stream.Collectors;

                @ControllerAdvice
                public class GlobalExceptionHandler {

                    // DTO validation error handling
                    @ExceptionHandler(MethodArgumentNotValidException.class)
                    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {

                        List<ApiError.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors()
                                .stream()
                                .map(err -> new ApiError.FieldError(err.getField(), err.getDefaultMessage()))
                                .collect(Collectors.toList());

                        ApiError error = new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                "Validation error",
                                request.getRequestURI(),
                                fieldErrors
                        );

                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                    }

                    // Parameter-level validation handling
                    @ExceptionHandler(ConstraintViolationException.class)
                    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {

                        List<ApiError.FieldError> fieldErrors = ex.getConstraintViolations()
                                .stream()
                                .map(cv -> {
                                    String field = cv.getPropertyPath().toString();
                                    if (field.contains(".")) {
                                        field = field.substring(field.lastIndexOf('.') + 1);
                                    }
                                    return new ApiError.FieldError(field, cv.getMessage());
                                })
                                .collect(Collectors.toList());

                        ApiError error = new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                "Validation error",
                                request.getRequestURI(),
                                fieldErrors
                        );

                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                    }

                    // Malformed JSON
                    @ExceptionHandler(HttpMessageNotReadableException.class)
                    public ResponseEntity<ApiError> handleMalformedJson(HttpMessageNotReadableException ex, HttpServletRequest request) {

                        ApiError error = new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                "Malformed JSON: " + Optional.ofNullable(ex.getMostSpecificCause())
                                        .map(Throwable::getMessage).orElse(ex.getMessage()),
                                request.getRequestURI()
                        );

                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                    }

                    // Resource not found
                    @ExceptionHandler(ResourceNotFoundException.class)
                    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

                        ApiError error = new ApiError(
                                HttpStatus.NOT_FOUND.value(),
                                "Not Found",
                                ex.getMessage(),
                                request.getRequestURI()
                        );

                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                    }

                    // Data integrity errors
                    @ExceptionHandler(DataIntegrityViolationException.class)
                    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {

                        ApiError error = new ApiError(
                                HttpStatus.CONFLICT.value(),
                                "Conflict",
                                "Database error: " + Optional.ofNullable(ex.getRootCause())
                                        .map(Throwable::getMessage).orElse(ex.getMessage()),
                                request.getRequestURI()
                        );

                        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
                    }

                    // General unexpected errors
                    @ExceptionHandler(Exception.class)
                    public ResponseEntity<ApiError> handleGeneral(Exception ex, HttpServletRequest request) {

                        // Optional logger: log critical errors
                        // LoggerFactory.getLogger(GlobalExceptionHandler.class).error("Unexpected error", ex);

                        ApiError error = new ApiError(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                Optional.ofNullable(ex.getMessage()).orElse("Unexpected error"),
                                request.getRequestURI()
                        );

                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
                    }
                }