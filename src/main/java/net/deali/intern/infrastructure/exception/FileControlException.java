package net.deali.intern.infrastructure.exception;

import lombok.Getter;

@Getter
public class FileControlException extends RuntimeException{
    private ErrorCode errorCode;

    public FileControlException() {
        super();
    }
    public FileControlException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public FileControlException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
