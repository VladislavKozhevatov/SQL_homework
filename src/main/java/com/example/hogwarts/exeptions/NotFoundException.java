package com.example.hogwarts.exeptions;
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
