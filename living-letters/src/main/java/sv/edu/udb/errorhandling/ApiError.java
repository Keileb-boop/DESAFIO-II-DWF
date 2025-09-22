package sv.edu.udb.errorhandling;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApiError {
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> fieldErrors = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    // Constructors
    public ApiError() {}

    public ApiError(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ApiError(int status, String error, String message, String path, List<FieldError> fieldErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        if (fieldErrors != null) {
            this.fieldErrors = new ArrayList<>(fieldErrors);
        }
    }

// Inner class for field errors
    public static final class FieldError {
        private String field;
        private String message;

        public FieldError() {}
        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    // Getters and setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public List<FieldError> getFieldErrors() { return fieldErrors; }
    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors != null ? new ArrayList<>(fieldErrors) : new ArrayList<>();
    }

    // Method to add individual field errors
    public void addFieldError(String field, String message) {
        this.fieldErrors.add(new FieldError(field, message));
    }
}
