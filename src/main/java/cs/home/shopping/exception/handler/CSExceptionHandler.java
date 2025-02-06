package cs.home.shopping.exception.handler;

import cs.home.shopping.dto.response.ErrorResponseDTO;
import cs.home.shopping.exception.InvalidOperationException;
import cs.home.shopping.exception.ItemNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CSExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(parseError(ex, HttpStatus.BAD_REQUEST, ex.getBindingResult()
            .getAllErrors()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidOperationException.class})
    public ResponseEntity<Object> handleException(InvalidOperationException ex) {
        return new ResponseEntity<>(parseError(ex, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ItemNotFoundException.class})
    public ResponseEntity<Object> handleException(ItemNotFoundException ex) {
        return new ResponseEntity<>(parseError(ex, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleGeneralException(RuntimeException ex) {
        return new ResponseEntity<>(parseError(ex, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponseDTO parseError(RuntimeException exception, HttpStatus status) {
        log.error("Exception handled: {}", exception.getMessage(), exception);
        return ErrorResponseDTO.builder()
            .httpStatus(ObjectUtils.isEmpty(status) ? "" : status.name())
            .message(exception.getMessage())
            .build();
    }

    private ErrorResponseDTO parseError(BindException exception, HttpStatus status, List<ObjectError> errorList) {
        log.error("Exception handled: {}", exception.getMessage(), exception);
        return ErrorResponseDTO.builder()
            .httpStatus(ObjectUtils.isEmpty(status) ? "" : status.name())
            .message("Request has one or more invalid attributes.")
            .errors(errorList.stream()
                .map(e -> ((FieldError) e).getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList()))
            .build();
    }
}
