package org.ilisi.backend.exception;

public class EntityNotFoundException extends GlobalAppException {
    public EntityNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }

    public EntityNotFoundException(String message) {
        super(message, "ENTITY_NOT_FOUND");
    }
}
