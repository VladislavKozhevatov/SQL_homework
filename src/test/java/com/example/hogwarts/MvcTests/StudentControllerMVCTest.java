package com.example.hogwarts.MvcTests;

import com.example.hogwarts.controller.StudentController;
import com.example.hogwarts.models.Faculty;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.repository.StudentRepository;
import com.example.hogwarts.service.FacultyService;
import com.example.hogwarts.service.StudentService;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class StudentControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentRepository studentRepository;

    @MockitoBean
    private FacultyRepository facultyRepositoryRepository;

    @MockitoSpyBean
    private StudentService studentService;

    @MockitoSpyBean
    private FacultyService facultyService;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void AddStudentTest() throws Exception {
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", "Антон");
        studentObject.put("age", 20);


    }

}
