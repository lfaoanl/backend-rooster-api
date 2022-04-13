package nl.faanveldhuijsen.roosters.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {
        public ResourceNotFound() {
            super();
        }
        public ResourceNotFound(String message, Throwable cause) {
            super(message, cause);
        }
        public ResourceNotFound(String message) {
            super(message);
        }
        public ResourceNotFound(Throwable cause) {
            super(cause);
    }

}
