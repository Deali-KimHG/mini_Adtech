package net.deali.intern.infrastructure.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "internal server error"),

    INVALID_INPUT_VALUE(400, "Invalid input value"),
    INVALID_INPUT_DATE(401, "Invalid input date"),
    INVALID_START_DATE(402, "Invalid start date"),
    INVALID_END_DATE(403, "Invalid end date"),
    INVALID_END_DATE_IN_EXPIRATION(404, "Invalid end date in expiration"),

    DELETED_CREATIVE(1001, "Access deleted creative is denied"),
    EXPIRED_CREATIVE(1002, "Creative was expired"),
    PAUSED_CREATIVE(1003, "Creative was paused"),
    FIND_CREATIVE_FAIL(1010, "Find creative failed"),
    FIND_ADVERTISEMENT_FAIL(1011, "Find advertisement failed"),

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
