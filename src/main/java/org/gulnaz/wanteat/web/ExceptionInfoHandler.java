package org.gulnaz.wanteat.web;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.gulnaz.wanteat.util.ValidationUtil;
import org.gulnaz.wanteat.util.exception.ErrorInfo;
import org.gulnaz.wanteat.util.exception.IllegalRequestDataException;
import org.gulnaz.wanteat.util.exception.NotFoundException;
import org.gulnaz.wanteat.util.exception.VoteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author gulnaz
 */
@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    private static final List<String> CONSTRAINS = List.of(
        "users_unique_email_idx",
        "restaurants_unique_name_address_idx",
        "dishes_unique_name_created_idx");

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, HttpStatus.UNPROCESSABLE_ENTITY, false);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, VoteException.class})
    public ResponseEntity<ErrorInfo> conflict(HttpServletRequest req, Exception e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            Optional<String> filtered = CONSTRAINS.stream().filter(lowerCaseMsg::contains).findAny();
            if (filtered.isPresent()) {
                return logAndGetErrorInfo(req, e, HttpStatus.UNPROCESSABLE_ENTITY, false);
            }
        }

        return logAndGetErrorInfo(req, e, HttpStatus.CONFLICT, true);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorInfo> bindValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
            .toArray(String[]::new);

        return logAndGetErrorInfo(req, e, HttpStatus.UNPROCESSABLE_ENTITY, false, details);
    }

    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class,
        HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorInfo> illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, HttpStatus.UNPROCESSABLE_ENTITY, false);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorInfo> accessDeniedError(HttpServletRequest req, AccessDeniedException e) {
        return logAndGetErrorInfo(req, e, HttpStatus.FORBIDDEN, false);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, HttpStatus.INTERNAL_SERVER_ERROR, true);
    }

    private static ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e,
        HttpStatus status, boolean logStackTrace, String... details) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, logStackTrace);
        return ResponseEntity.status(status)
            .body(new ErrorInfo(req.getRequestURL(),
                details.length != 0 ? details : new String[]{rootCause.getMessage()})
            );
    }
}
