package net.deali.intern.infrastructure.exception;

import lombok.Getter;

@Getter
public class FileControlException extends RuntimeException{
    private final ErrorCode errorCode;

    public FileControlException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
