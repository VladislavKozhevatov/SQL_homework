package com.example.hogwarts.controller;
import com.example.hogwarts.models.Faculty;
import com.example.hogwarts.models.Student;
import com.example.hogwarts.service.FacultyService;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @GetMapping("{id}")
    public Faculty findFaculty(@PathVariable long id) {
        return facultyService.findFaculty(id);
    }

    //STREAM-API

    @GetMapping("/getLongestFacultyName")
    public String getLongestFacultyName() {
        return facultyService.getLongestFacultyName();
    }

    @GetMapping("/getStreamParallelAmount")
    public Integer getStreamParallelAmount(){
        return facultyService.getStreamParallelAmount();
    }

    @GetMapping("/color")
    public Collection<Faculty> getFacultiesByColor(@RequestParam String color) {
        return facultyService.getFacultiesByColor(color);
    }

    @GetMapping("/colorOrName")
    public Faculty getFacultyByColorOrName(@RequestParam(required = false) String color,
                                           @RequestParam(required = false) String name) {
        return facultyService.findFacultyByColorOrName(color, name);
    }

    @GetMapping()
    public Collection<Faculty> getAll() {
        return facultyService.getAllFaculties();
    }

    @GetMapping("/idFaculty-by-students")
    public Collection<Student> getStudentsByFacultyId(@RequestParam long facultyId) {
        return facultyService.findFaculty(facultyId).getStudents();
    }

    @PutMapping()
    public Faculty changeFaculty(@RequestBody Faculty faculty) {
        return facultyService.changeFaculty(faculty);
    }

    @DeleteMapping("{id}")
    public void deleteFaculty(@PathVariable long id) {
        facultyService.deleteFaculty(id);
    }
}
