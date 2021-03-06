package com.hackathon.MYD.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessage {
    INVALID_TOKEN(401,"Invalid Token"),
    INVALID_SIGNATURE(401, "Invalid signature"),
    TOKEN_EXPIRED(401, "Token expired"),
    UNSUPPORTED_TOKEN(401, "Unsupported token"),
    USER_NOT_FOUND(404,"User Not Found"),
    STORE_NOT_FOUND(404, "STORE_NOT_FOUND");

    private final int status;

    private final String message;
}
