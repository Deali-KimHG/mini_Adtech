package net.deali.intern.infrastructure.exception;

import lombok.Getter;

@Getter
public class EntityControlException extends RuntimeException {
    private final ErrorCode errorCode;

    public EntityControlException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
