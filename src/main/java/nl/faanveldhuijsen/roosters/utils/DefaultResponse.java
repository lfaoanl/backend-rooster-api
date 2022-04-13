package nl.faanveldhuijsen.roosters.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.HashMap;

@Service
public class DefaultResponse {

    private static class FieldError extends HashMap<String, String> {
        public FieldError(org.springframework.validation.FieldError error) {
            this.put("provided", (String) error.getRejectedValue());
            this.put("field", error.getField());
            this.put("message", error.getDefaultMessage());
        }
    }

    public ResponseEntity<Object> error(Object message, HttpStatus status) {
        return this.response(message, status, true);
    }

    public ResponseEntity<Object> ok(Object message) {
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    public ResponseEntity<Object> created(Object message) {
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> ok(String message) {
        return this.response(message, HttpStatus.OK);
    }

    public ResponseEntity<Object> response(Object message, HttpStatus status) {
        return this.response(message, status, false);
    }

    public ResponseEntity<Object> response(Object message, HttpStatus status, boolean error) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", status.value());
        response.put("message", message);

        if (error) {
            response.put("error", status.getReasonPhrase());
        }

        return new ResponseEntity<>(response, status);
    }

    private HashMap<String, FieldError> parseFieldErrors(BindingResult result) {

        HashMap<String, FieldError> errors = new HashMap<>();

        for (org.springframework.validation.FieldError error : result.getFieldErrors()) {
            FieldError fieldError = new DefaultResponse.FieldError(error);
            errors.put(error.getField(), fieldError);
        }

        return errors;
    }

    public ResponseEntity<Object> fieldErrors(BindingResult result) {
        return new ResponseEntity<>(this.parseFieldErrors(result), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> notFound(String message) {
        return this.error(message, HttpStatus.NOT_FOUND);
    }

}
