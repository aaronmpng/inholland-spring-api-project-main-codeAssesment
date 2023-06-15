package io.inholland.groep4.api;

import io.inholland.groep4.api.model.DTO.ExceptionDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UniversalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ResponseStatusException.class })
    protected ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        HttpStatus status = ex.getStatus();
        String errorMessage = ex.getReason();

        if (status == HttpStatus.UNPROCESSABLE_ENTITY) {
            String exceptionMessage = getExceptionMessage(errorMessage);
            if (exceptionMessage != null) {
                ExceptionDTO dto = new ExceptionDTO(exceptionMessage);
                return handleExceptionInternal(ex, dto, new HttpHeaders(), status, request);
            }
        }

        if (status == HttpStatus.BAD_REQUEST) {
            String exceptionMessage = getExceptionBadRequest(errorMessage);
            if (exceptionMessage != null) {
                ExceptionDTO dto = new ExceptionDTO(exceptionMessage);
                return handleExceptionInternal(ex, dto, new HttpHeaders(), status, request);
            }
        }

        if (status == HttpStatus.NOT_FOUND) {
            String exceptionMessage = getExceptionNotFound(errorMessage);
            if (exceptionMessage != null) {
                ExceptionDTO dto = new ExceptionDTO(exceptionMessage);
                return handleExceptionInternal(ex, dto, new HttpHeaders(), status, request);
            }
        }
        if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            String exceptionMessage = getExceptionInternalServerError(errorMessage);
            if (exceptionMessage != null) {
                ExceptionDTO dto = new ExceptionDTO(exceptionMessage);
                return handleExceptionInternal(ex, dto, new HttpHeaders(), status, request);
            }
        }

        if (status == HttpStatus.FORBIDDEN) {
            String exceptionMessage = getExceptionForbidden(errorMessage);
            if (exceptionMessage != null) {
                ExceptionDTO dto = new ExceptionDTO(exceptionMessage);
                return handleExceptionInternal(ex, dto, new HttpHeaders(), status, request);
            }
        }

        // Handle other ResponseStatusExceptions
        ExceptionDTO dto = new ExceptionDTO("Something went wrong, please check again!");
        return handleExceptionInternal(ex, dto, new HttpHeaders(), status, request);
    }

    private String getExceptionMessage(String errorMessage) {
        switch (errorMessage) {
            case "Account not found":
                return "Account not found";
            case "Account does not belong to owner":
                return "Account does not belong to owner";
            default:
                return null;
        }
    }

    private String getExceptionNotFound(String errorMessage) {
        switch (errorMessage) {
            case "User accounts not found":
                return "User accounts not found";
            case "No account to save":
                return "No account to save";
            case "Account not found":
                return "Account not found";
            default:
                return null;
        }
    }

    private String getExceptionInternalServerError(String errorMessage) {
        switch (errorMessage) {
            case "Error retrieving user accounts":
                return "Error retrieving user accounts";
            default:
                return null;
        }
    }

    private String getExceptionForbidden(String errorMessage) {
        switch (errorMessage) {
            case "Access denied":
                return "Access denied";
            default:
                return null;
        }
    }

    private String getExceptionBadRequest(String errorMessage) {
        switch (errorMessage) {
            case "Incorrect account type given":
                return "Incorrect account type provided";
            case "Incorrect lower limit given":
                return "Incorrect lower limit provided";
            case "Incorrect account status given":
                return "Incorrect account status provided";
            case "Incorrect account balance given":
                return "Incorrect account balance provided";
            case "Incorrect iban given":
                return "Incorrect iban given";
            default:
                return null;
        }
    }
}

