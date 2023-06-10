package com.umeshgiri.drones.exception;

import com.umeshgiri.drones.payload.CommonResponse;
import com.umeshgiri.drones.payload.CommonResponseStatus;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<CommonResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ex.printStackTrace();
        final CommonResponse.CommonResponseBuilder message = CommonResponse.builder()
                .status(CommonResponseStatus.FAILURE)
                .message(ex.getLocalizedMessage());
        return new ResponseEntity<>(message.build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public final ResponseEntity<CommonResponse> handleIllegalArgumentExceptions(Exception ex, WebRequest request) {
        final CommonResponse.CommonResponseBuilder message = CommonResponse.builder()
                .status(CommonResponseStatus.FAILURE)
                .message(ex.getLocalizedMessage());
        return new ResponseEntity<>(message.build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public final ResponseEntity<CommonResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .toList();

        final CommonResponse.CommonResponseBuilder message = CommonResponse.builder()
                .status(CommonResponseStatus.FAILURE)
                .message("Validation error")
                .errors(errors);

        return new ResponseEntity<>(message.build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public final ResponseEntity<CommonResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        ex.printStackTrace();
        String errorMessage = "Data integrity violation";

        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            String constraintName = constraintViolationException.getConstraintName();
            String sqlMessage = constraintViolationException.getSQLException().getMessage();

            if (constraintName != null && constraintName.contains("UK_")) {
                String violatedColumn = extractViolatedColumnName(sqlMessage);
                errorMessage = "Duplicate not allowed for " + violatedColumn;
            }
        }

        final CommonResponse.CommonResponseBuilder message = CommonResponse.builder()
                .status(CommonResponseStatus.FAILURE)
                .message(errorMessage);

        return new ResponseEntity<>(message.build(), HttpStatus.CONFLICT);
    }

    private String extractViolatedColumnName(String sqlMessage) {
        Pattern pattern = Pattern.compile("\\((\\w+)");
        Matcher matcher = pattern.matcher(sqlMessage);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "unknown";
    }


}