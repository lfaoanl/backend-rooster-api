package nl.faanveldhuijsen.roosters.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidFileFormat extends RuntimeException {
        public InvalidFileFormat() {
            super();
        }
        public InvalidFileFormat(String message, Throwable cause) {
            super(message, cause);
        }
        public InvalidFileFormat(String message) {
            super(message);
        }
        public InvalidFileFormat(Throwable cause) {
            super(cause);
    }

}
