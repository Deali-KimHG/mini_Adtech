package net.deali.intern.infrastructure.exception;

import lombok.Getter;

@Getter
public class InputDataNotValidException extends RuntimeException {
    private final ErrorCode errorCode;

    public InputDataNotValidException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
