package com.cjfc.recipesmanager.domain.error

import com.cjfc.recipesmanager.domain.error.Severity.ERROR

enum class ErrorType(
    val code: String,
    val message: String,
    val severity: Severity
) {
    /**
     * GENERIC_ERROR Generic error.
     */
    GENERIC_ERROR("GENERIC_ERROR", "Generic error", ERROR),

    /**
     * INTERNAL_REMOTE_SERVER_ERROR Remote server internal error.
     */
    INTERNAL_REMOTE_SERVER_ERROR(
        "INTERNAL_REMOTE_SERVER_ERROR",
        "Remote server responded with an internal server error",
        ERROR
    ),

    /**
     * REMOTE_SERVER_NOT_RESPONDING_ERROR Remote server seems down.
     */
    REMOTE_SERVER_NOT_RESPONDING_ERROR(
        "REMOTE_SERVER_NOT_RESPONDING_ERROR",
        "Remote server seems down",
        ERROR
    ),

    /**
     * NOT_FOUND_ERROR Not Found Error.
     */
    NOT_FOUND_ERROR("NOT_FOUND_ERROR", "Not Found Error", ERROR),

    /**
     * VALIDATION_ERROR Some of provided parameters are not valid.
     */
    VALIDATION_ERROR("VALIDATION_ERROR", "Provided parameters are not valid", ERROR),

    /**
     * SERVER_TOO_BUSY The server has too many concurrent connections and it can't attend them all.
     */
    SERVER_TOO_BUSY("SERVER_TOO_BUSY", "The server is too busy and it couln't handle the request", ERROR);
}