package com.cjfc.recipesmanager.handler

import com.cjfc.recipesmanager.domain.error.ErrorType
import com.cjfc.recipesmanager.domain.error.ErrorType.GENERIC_ERROR
import com.cjfc.recipesmanager.domain.error.ErrorType.NOT_FOUND_ERROR
import com.cjfc.recipesmanager.domain.error.ErrorType.REMOTE_SERVER_NOT_RESPONDING_ERROR
import com.cjfc.recipesmanager.domain.error.ErrorType.SERVER_TOO_BUSY
import com.cjfc.recipesmanager.domain.error.ErrorType.VALIDATION_ERROR
import com.cjfc.recipesmanager.domain.error.RecipesManagerException
import com.cjfc.recipesmanager.presentation.payload.error.ErrorDetails
import com.cjfc.recipesmanager.presentation.payload.error.ErrorPayload
import com.cjfc.recipesmanager.presentation.payload.error.Severity
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Class used to manage in a centralized way, all the possible exceptions raised by the application and then convert
 * them to a proper error payload to be displayed to the user.
 * <br></br>
 * This manager is able to handle errors discriminating them by:
 *
 *  * **Application exceptions:** Any exception throw by the application which was captured and translated to a RecipesManagerException exception.
 *  * **Validation exceptions:** Exceptions raised due to wrong values passed when calling the API, such as date format
 *  * **Unmanaged exceptions:** Unexpected exceptions which weren't managed, and were able to get above the resource layer, like NPE
 *
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Method in charge of handling all the runtime exceptions which were managed and then converted to RecipesManagerException.
     *
     * @param exception the RecipesManagerException[] raised exception.
     * @return a Response with an [ErrorPayload] and the mapped http code for it.
     */
    @ExceptionHandler(RecipesManagerException::class)
    @ResponseStatus
    fun handleRecipesManagerException(exception: RecipesManagerException): ResponseEntity<ErrorPayload> {
        log.error(
            "Received application exception: {}, with message: {}", exception.javaClass.name,
            exception.message
        )

        log.trace("Error trace: ", exception)

        return ResponseEntity(
            createErrorPayload(exception.errorType),
            ERROR_TYPE_TO_HTTP_STATUS_MAPPING[exception.errorType] ?: INTERNAL_SERVER_ERROR
        )
    }

    /**
     * Method in charge of handling all the input validation exceptions raised by Spring.
     *
     * @param exception the input validation exception.
     * @return an ErrorPayload and a http code 400 (Bad Request).
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(BAD_REQUEST)
    fun handleValidationException(exception: MethodArgumentNotValidException): ErrorPayload {
        log.error(
            "Received validation exception: {}, with message: {}", exception.javaClass.name,
            exception.message
        )

        log.trace("Error trace: ", exception)

        return createErrorPayload(VALIDATION_ERROR)
    }

    /**
     * Method in charge of handling unexpected / non handled exceptions.
     *
     * @param exception a unexpected / non handled exception.
     * @return an ErrorPayload and a http code 500 (Internal Sever Error).
     */
    @ExceptionHandler(Exception::class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    fun handleGeneralException(exception: Exception): ErrorPayload {
        log.error(
            "Received unmanaged exception: {}, with message: {}", exception.javaClass.name,
            exception.message
        )

        log.trace("Error trace: ", exception)

        return createErrorPayload(GENERIC_ERROR)
    }

    companion object {
        private val ERROR_TYPE_TO_HTTP_STATUS_MAPPING: Map<ErrorType, HttpStatus> = mapOf(
            GENERIC_ERROR to INTERNAL_SERVER_ERROR,
            NOT_FOUND_ERROR to NOT_FOUND,
            VALIDATION_ERROR to BAD_REQUEST,
            SERVER_TOO_BUSY to SERVICE_UNAVAILABLE,
            REMOTE_SERVER_NOT_RESPONDING_ERROR to INTERNAL_SERVER_ERROR
        )

        private fun createErrorPayload(errorType: ErrorType): ErrorPayload =
            ErrorPayload(
                ErrorDetails(
                    code = errorType.code,
                    message = errorType.message,
                    severity = Severity.valueOf(errorType.severity.toString())
                )
            )
    }
}