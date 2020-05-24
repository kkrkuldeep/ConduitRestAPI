package com.mountblue.canduitRestAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostRestExceptionHandler {

    // add an exception handler for Post Not Found class
    @ExceptionHandler
    public ResponseEntity<PostErrorResponse> handleException(PostNotFoundException exception){

        // create  a customer response entity
        PostErrorResponse errorResponse = new PostErrorResponse(HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),System.currentTimeMillis());

        // return Response Entity(Body, Status code)
        return  new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Add another exception handler .... to catch alll exception
    @ExceptionHandler
    public ResponseEntity<PostErrorResponse> handleException(Exception exception){

        // create  a customer response entity
        PostErrorResponse errorResponse = new PostErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),System.currentTimeMillis());

        // return Response Entity(Body, Status code)
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
