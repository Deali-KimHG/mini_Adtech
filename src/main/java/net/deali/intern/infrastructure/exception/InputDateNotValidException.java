package net.deali.intern.infrastructure.exception;

import lombok.Getter;

@Getter
public class InputDateNotValidException extends RuntimeException {
    private final ErrorCode errorCode;

    public InputDateNotValidException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
