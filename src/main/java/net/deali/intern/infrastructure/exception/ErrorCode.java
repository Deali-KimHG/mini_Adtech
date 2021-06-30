package net.deali.intern.infrastructure.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "internal server error"),

    INVALID_INPUT_DATA(400, "Invalid input value"),

    DELETED_CREATIVE(1001, "Access deleted creative is denied"),
    FIND_CREATIVE_FAIL(1002, "Find creative failed"),
    FIND_ADVERTISEMENT_FAIL(1011, "Find advertisement failed"),
//    NOT_ADVERTISE_YET(1012, "Not advertise yet"),

    FILE_NOT_FOUND(901, "File not found"),
    FILE_ALREADY_DELETED(902, "File already deleted"),
    DIRECTORY_CREATION_FAIL(903, "Directory creation fail"),
    FILE_CREATION_FAIL(904, "File creation fail"),
    INVALID_FILE_NAME(905, "Invalid file name");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
