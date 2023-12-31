package org.ilisi.backend.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@ToString
public class GlobalAppException extends RuntimeException {
    private final String errorCode;
    private final LocalDateTime timestamp;
    private final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public GlobalAppException(String message, String errorCode, HttpStatus status) {
        this(message, errorCode, LocalDateTime.now());
    }

    public GlobalAppException(String message, String errorCode, Throwable cause) {
        this(message, cause, errorCode, LocalDateTime.now());
    }

    public GlobalAppException(Throwable cause) {
        this(cause, "GLOBAL_APP_ERROR", LocalDateTime.now());
    }

    public GlobalAppException(String message, String errorCode, LocalDateTime timestamp) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    public GlobalAppException(String message, Throwable cause, String errorCode, LocalDateTime timestamp) {
        super(message, cause);
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    public GlobalAppException(Throwable cause, String errorCode, LocalDateTime timestamp) {
        super(cause);
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }


}