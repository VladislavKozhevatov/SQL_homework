package com.example.hogwarts.controller;

import com.example.hogwarts.models.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portInfo")
public class InfoController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/port")
    public String getPort() {
        return "Application is running on port:"+ serverPort;
    }
}
