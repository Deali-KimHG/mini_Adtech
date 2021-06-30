package net.deali.intern.infrastructure.exception;

import lombok.Getter;

@Getter
public class EntityControlException extends RuntimeException {
    private ErrorCode errorCode;

    public EntityControlException() {
        super();
    }
    public EntityControlException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public EntityControlException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
