package net.deali.intern.infrastructure.exception;

import lombok.Getter;

@Getter
public class DeletedCreativeException extends RuntimeException {
    private ErrorCode errorCode;

    public DeletedCreativeException() {
        super();
    }
    public DeletedCreativeException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public DeletedCreativeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
