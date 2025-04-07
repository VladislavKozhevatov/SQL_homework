package com.example.hogwarts.service;

import com.example.hogwarts.models.Student;

import java.util.List;

public interface StudentService {

    public List<Student> getStudentsByName(String name);

    Student addStudent(Student student);

    Student findStudent(long id);

    List<Student> getAllStudents();

    Student changeStudent(Student student);

    void deleteStudent(long id);

    List<Student> getStudentsByAge(int age);

    List<Student> findStudentsByAgeBetween(int minAge, int maxAge);

    public Integer getTotalNumberOfStudents();

    public Double getAvgAgeOfStudents();

   public List<Student> getLast5Students();

   //STREAM API

    List<String> findAllStudentsWhichNameStarts(String letter);

    Integer getAverageAgeStudents();

// ПОТОКИ
    public void getStudentsPrintParallel();
    public void getStudentsPrintSynchronized();
}
