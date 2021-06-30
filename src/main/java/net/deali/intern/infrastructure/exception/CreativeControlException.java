package net.deali.intern.infrastructure.exception;

import lombok.Getter;

@Getter
public class CreativeControlException extends RuntimeException {
    private ErrorCode errorCode;

    public CreativeControlException() {
        super();
    }
    public CreativeControlException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public CreativeControlException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
