package com.example.hogwarts.service;

import com.example.hogwarts.exeptions.NotFoundException;
import com.example.hogwarts.models.Student;
import com.example.hogwarts.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student findStudent(long id) {
        validateId(id);
        return studentRepository.findById(id).get();
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getStudentsByAge(int age) {
        validateAge(age);
        return studentRepository.findByAge(age);
    }

    @Override
    public Student changeStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(long id) {
        validateId(id);
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> findStudentsByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }


    private void validateId(long id) {
        if (studentRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Студент с id = " + id + " не существует");
        }
    }

    private void validateAge(int age) {
        if (studentRepository.findByAge(age).isEmpty()) {
            throw new NotFoundException("Студента с возрастом - " + age + " не существует");
        }
    }

    @Override
    public Integer getTotalNumberOfStudents() {
        return studentRepository.getTotalNumberOfStudents();
    }

    @Override
    public Double getAvgAgeOfStudents(){
        return studentRepository.getAvgAgeOfStudents();
    }

    @Override
    public List <Student> getLast5Students(){
        return studentRepository.getLast5Students();
    }
}
