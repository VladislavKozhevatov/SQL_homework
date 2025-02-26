package com.example.hogwarts.service;

import com.example.hogwarts.models.Student;

import java.util.List;

public interface StudentService {
    Student addStudent(Student student);

    Student findStudent(long id);

    List<Student> getAllStudents();

    Student changeStudent(Student student);

    void deleteStudent(long id);

    List<Student> getStudentsByAge(int age);

    List<Student> findStudentsByAgeBetween(int minAge, int maxAge);
}
