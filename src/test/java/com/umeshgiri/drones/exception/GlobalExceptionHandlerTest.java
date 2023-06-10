package com.umeshgiri.drones.exception;

import com.umeshgiri.drones.payload.CommonResponse;
import com.umeshgiri.drones.payload.CommonResponseStatus;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @DisplayName("Should return a conflict response when DataIntegrityViolationException occurs")
    void handleDataIntegrityViolationWhenExceptionOccurs() {
        DataIntegrityViolationException ex = mock(DataIntegrityViolationException.class);
        WebRequest request = mock(WebRequest.class);
        ConstraintViolationException constraintViolationException = mock(ConstraintViolationException.class);
        SQLException sqlException = mock(SQLException.class);

        when(ex.getCause()).thenReturn(constraintViolationException);
        when(constraintViolationException.getConstraintName()).thenReturn("UK_test");
        when(constraintViolationException.getSQLException()).thenReturn(sqlException);
        when(sqlException.getMessage()).thenReturn("Unique index or primary key violation: \"PUBLIC.UK_LSJVI6H63GA5X3QWVO5VOQ1F8_INDEX_7 ON PUBLIC.DRONES(test NULLS FIRST) VALUES ( /* 1 */ '12345' )\";");

        ResponseEntity<CommonResponse> responseEntity = globalExceptionHandler.handleDataIntegrityViolation(ex, request);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Duplicate not allowed for test", responseEntity.getBody().getMessage());
        assertEquals(CommonResponseStatus.FAILURE, responseEntity.getBody().getStatus());
    }

    @Test
    @DisplayName("Should return a conflict response with a custom message when a unique constraint is violated")
    void handleDataIntegrityViolationWhenUniqueConstraintIsViolated() {
        DataIntegrityViolationException dataIntegrityViolationException = mock(DataIntegrityViolationException.class);
        ConstraintViolationException constraintViolationException = mock(ConstraintViolationException.class);
        SQLException sqlException = mock(SQLException.class);
        WebRequest webRequest = mock(WebRequest.class);

        when(dataIntegrityViolationException.getCause()).thenReturn(constraintViolationException);
        when(constraintViolationException.getConstraintName()).thenReturn("UK_test");
        when(constraintViolationException.getSQLException()).thenReturn(sqlException);
        when(sqlException.getMessage()).thenReturn("Unique index or primary key violation: \"PUBLIC.UK_LSJVI6H63GA5X3QWVO5VOQ1F8_INDEX_7 ON PUBLIC.DRONES(test NULLS FIRST) VALUES ( /* 1 */ '12345' )\";");

        ResponseEntity<CommonResponse> responseEntity = globalExceptionHandler.handleDataIntegrityViolation(dataIntegrityViolationException, webRequest);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Duplicate not allowed for test", responseEntity.getBody().getMessage());
        assertEquals(CommonResponseStatus.FAILURE, responseEntity.getBody().getStatus());
    }

    @Test
    @DisplayName("Should return a conflict response with a generic message when no unique constraint is violated")
    void handleDataIntegrityViolationWhenNoUniqueConstraintIsViolated() {// create a mock DataIntegrityViolationException
        DataIntegrityViolationException dataIntegrityViolationException = mock(DataIntegrityViolationException.class);

        // create a mock WebRequest
        WebRequest webRequest = mock(WebRequest.class);

        // create a mock ConstraintViolationException
        ConstraintViolationException constraintViolationException = mock(ConstraintViolationException.class);

        // create a mock SQLException
        SQLException sqlException = mock(SQLException.class);

        // set the SQLException message
        when(sqlException.getMessage()).thenReturn("Unique index or primary key violation: \"PUBLIC.UK_LSJVI6H63GA5X3QWVO5VOQ1F8_INDEX_7 ON PUBLIC.DRONES(name NULLS FIRST) VALUES ( /* 1 */ '12345' )\";");

        // set the ConstraintViolationException properties
        when(constraintViolationException.getConstraintName()).thenReturn("UK_name");
        when(constraintViolationException.getSQLException()).thenReturn(sqlException);

        // set the DataIntegrityViolationException cause
        when(dataIntegrityViolationException.getCause()).thenReturn(constraintViolationException);

        // call the method under test
        ResponseEntity<CommonResponse> responseEntity = globalExceptionHandler.handleDataIntegrityViolation(dataIntegrityViolationException, webRequest);

        // assert the response status code
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

        // assert the response message
        assertEquals("Duplicate not allowed for name", responseEntity.getBody().getMessage());

        // assert the response status
        assertEquals(CommonResponseStatus.FAILURE, responseEntity.getBody().getStatus());
    }

}