package com.example.hogwarts.MvcTests;

import com.example.hogwarts.controller.FacultyController;
import com.example.hogwarts.models.Faculty;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.service.FacultyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class FacultyConrollerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyRepository facultyRepository;

    @MockitoSpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    public void addFacultyTest1() throws Exception {
        final String name = "2121212";
        final String color = "asas";
        final long id = 1;

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findByColor(any(String.class))).thenReturn(List.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }


    @Test
    void findFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "red");
        when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.color").value("red"));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/{id}", 999L))
                .andExpect(status().isNotFound());
    }
//
//    @Test
//    void getFacultiesByColorTest() throws Exception {
//        Faculty faculty = new Faculty(1L, "Gryffindor", "red", Collections.emptyList());
//        when(facultyService.getFacultiesByColor("red")).thenReturn(List.of(faculty));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/color")
//                        .param("color", "red"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
//                .andExpect(jsonPath("$.size()").value(1));
//    }
//
//    @Test
//    void getFacultyByColorOrNameTest() throws Exception {
//        Faculty faculty = new Faculty(1L, "Gryffindor", "red", Collections.emptyList());
//        when(facultyService.findFacultyByColorOrName("red", null)).thenReturn(faculty);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/colorOrName")
//                        .param("color", "red"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L));
//
//        verify(facultyService).findFacultyByColorOrName("red", null);
//    }
//
//    @Test
//    void getAllFacultiesTest() throws Exception {
//        Faculty faculty1 = new Faculty(1L, "Gryffindor", "red", Collections.emptyList());
//        Faculty faculty2 = new Faculty(2L, "Slytherin", "green", Collections.emptyList());
//        when(facultyService.getAllFaculties()).thenReturn(List.of(faculty1, faculty2));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/faculty"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(2))
//                .andExpect(jsonPath("$[1].name").value("Slytherin"));
//    }
//
//    @Test
//    void getStudentsByFacultyIdTest() throws Exception {
//        Student student = new Student(1L, "Harry Potter", 11);
//        Faculty faculty = new Faculty(1L, "Gryffindor", "red", List.of(student));
//        when(facultyService.findFaculty(1L)).thenReturn(faculty);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/idFaculty-by-students")
//                        .param("facultyId", "1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("Harry Potter"));
//    }
//
//    @Test
//    void changeFacultyTest() throws Exception {
//        Faculty updatedFaculty = new Faculty(1L, "New Gryffindor", "blue", Collections.emptyList());
//        when(facultyService.changeFaculty(any())).thenReturn(updatedFaculty);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/faculty")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedFaculty)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("New Gryffindor"));
//    }
//
//    @Test
//    void deleteFacultyTest() throws Exception {
//        doNothing().when(facultyService).deleteFaculty(1L);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/{id}", 1L))
//                .andExpect(status().isOk());
//
//        verify(facultyService, times(1)).deleteFaculty(1L);
//    }
}
