package com.example.hogwarts.MvcTests;

import com.example.hogwarts.controller.FacultyController;
import com.example.hogwarts.controller.StudentController;
import com.example.hogwarts.models.Faculty;
import com.example.hogwarts.models.Student;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.service.FacultyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;


import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Arrays;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyConrollerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FacultyRepository facultyRepository;

    @MockitoBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    public void testAddFaculty() throws Exception {
        // Создаем тестовый объект Faculty
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        // Мокируем поведение сервиса
        Mockito.when(facultyService.addFaculty(Mockito.any(Faculty.class))).thenReturn(faculty);

        // Выполняем POST-запрос
        ResultActions perform =
                mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)));

        perform
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(faculty.getId())) // Проверяем поле id
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(faculty.getName())) // Проверяем поле name
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(faculty.getColor())); // Проверяем поле color

    }


    @Test
    void findFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        Mockito.when(facultyService.findFaculty(1L)).thenReturn(faculty);

        ResultActions perform =
                mockMvc.perform(MockMvcRequestBuilders.get("/faculty/{id}", 1L))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1L))
                        .andExpect(jsonPath("$.color").value("red"));

    }

    @Test
    void changeFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        when(facultyService.changeFaculty(any())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        doNothing().when(facultyService).deleteFaculty(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/{id}", 1L))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).deleteFaculty(1L);
    }

    @Test
    void getFacultiesByColorTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        when(facultyService.getFacultiesByColor("red")).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/color")
                        .param("color", "red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void getFacultyByColorOrNameTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        
        when(facultyService.findFacultyByColorOrName("red", null)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/colorOrName")
                        .param("color", "red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(facultyService).findFacultyByColorOrName("red", null);
    }

    @Test
    void getAllFacultiesTest() throws Exception {

        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Gryffindor");
        faculty1.setColor("red");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("Slytherin");
        faculty2.setColor("green");


        when(facultyService.getAllFaculties()).thenReturn(List.of(faculty1, faculty2));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[1].name").value("Slytherin"));
    }

    @Test
    void getStudentsByFacultyIdTest() throws Exception {
        //Добавляем нового студента
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(11);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Hermione Granger");
        student.setAge(11);

        //Добавляем новый факультет
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("red");
        List.of(student);

        faculty.setStudents(Arrays.asList(student,student2));

        when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/idFaculty-by-students")
                        .param("facultyId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
         //               .content(objectMapper.writeValueAsString(faculty)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[1].name").value("Hermione Granger"));

    }
}

//
//    @Test
//    public void addFacultyTest1() throws Exception {
//        final String name = "2121212";
//        final String color = "asas";
//        final long id = 1;
//
//        JSONObject facultyObject = new JSONObject();
//        facultyObject.put("name", name);
//        facultyObject.put("color", color);
//
//        Faculty faculty = new Faculty();
//        faculty.setId(id);
//        faculty.setName(name);
//        faculty.setColor(color);
//
//        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
//        when(facultyRepository.findByColor(any(String.class))).thenReturn(List.of(faculty));
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/faculty")
//                        .content(facultyObject.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.name").value(name))
//                .andExpect(jsonPath("$.color").value(color));
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/faculty")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.name").value(name))
//                .andExpect(jsonPath("$.color").value(color));
//    }
//}
