package org.ilisi.backend.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends GlobalAppException {
    public EntityNotFoundException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.NOT_FOUND);
    }

    public EntityNotFoundException(String message) {
        super(message, "ENTITY_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
