package org.ilisi.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthenticationFailedException extends GlobalAppException {
    public AuthenticationFailedException(String message, String errorCode) {
        super(message, errorCode);
    }

    public AuthenticationFailedException(String message) {
        super(message, "AUTHENTICATION_FAILED");
    }
}
