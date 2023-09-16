package com.zhaofn.staybooking.controller;

import com.zhaofn.staybooking.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {

    //如果没有这个的话，返回的则是500 server error, 但是这个并不是server的error, 所以需要告诉server要怎么处理这个exception
    @ExceptionHandler(UserAlreadyExistException.class)
    public final ResponseEntity<String> handleUserAlreadyException(Exception ex, WebRequest request) {//WebRequest不写也没问题
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotExistException.class)
    public final ResponseEntity<String> handleUserNotExistException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(StayNotExistException.class)
    public final ResponseEntity<String> handleStayNotExistException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);//404
    }

    @ExceptionHandler(GCSUploadException.class)
    public final ResponseEntity<String> handleGCSUploadException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);//500系列的错误，不是backend的错是server的问题
    }

    @ExceptionHandler(InvalidSearchDateException.class)
    public final ResponseEntity<String> handleInvalidSearchDateExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GeoCodingException.class)
    public final ResponseEntity<String> handleGeoCodingExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidStayAddressException.class)
    public final ResponseEntity<String> handleInvalidStayAddressExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public final ResponseEntity<String> handleReservationNotFoundExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StayDeleteException.class)
    public final ResponseEntity<String> handleStayDeleteExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

}
