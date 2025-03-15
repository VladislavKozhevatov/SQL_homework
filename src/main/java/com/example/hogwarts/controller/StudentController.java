package com.example.hogwarts.controller;
import com.example.hogwarts.models.Faculty;
import com.example.hogwarts.models.Student;
import com.example.hogwarts.service.StudentService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }


    @GetMapping("/{id}")
    public Student findStudent(@PathVariable long id) {
        return studentService.findStudent(id);
    }

    @GetMapping("/age")
    public Collection<Student> getStudentsByAge(@RequestParam int age) {
        return studentService.getStudentsByAge(age);
    }

    @GetMapping
    public Collection<Student> getAll() {
        return studentService.getAllStudents();
    }

    @GetMapping("/age-between")
    public Collection<Student> getStudentsByAge(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.findStudentsByAgeBetween(minAge, maxAge);
    }


    @PutMapping("/{id}")
    public Student changeStudent(@RequestBody Student student) {

        return studentService.changeStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/countNumber")
    public ResponseEntity<Integer> getTotalNumberOfStudents() {
        Integer totalCount = studentService.getTotalNumberOfStudents();
        return ResponseEntity.ok(totalCount);
    }

    @GetMapping("/countAvgAge")
    public ResponseEntity<Double> getAvgAgeOfStudents() {
        Double averageAge = studentService.getAvgAgeOfStudents();
        return ResponseEntity.ok(averageAge);
    }

//    @GetMapping("/countAvgAge")
//    public ResponseEntity <List<Student>> getLast5Students() {
//        List<Student> lastFive = studentService.getLast5Students();
//        return ResponseEntity.ok(lastFive);
//    }
}
