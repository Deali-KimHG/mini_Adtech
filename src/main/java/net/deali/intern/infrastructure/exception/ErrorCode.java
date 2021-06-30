package net.deali.intern.infrastructure.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "internal server error"),

    INVALID_INPUT_DATA(400, "Invalid input value"),

    DELETED_CREATIVE(1001, "Access deleted creative is denied"),
    FIND_CREATIVE_FAIL(1002, "Find creative failed"),

    FILE_NOT_FOUND(1010, "File not found"),
    FILE_ALREADY_DELETED(1011, "File already deleted"),
    DIRECTORY_CREATION_FAIL(1012, "Directory creation fail"),
    FILE_CREATION_FAIL(1013, "File creation fail"),
    INVALID_FILE_NAME(1014, "Invalid file name");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
