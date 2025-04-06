package com.example.hogwarts.service;

import com.example.hogwarts.exeptions.NotFoundException;
import com.example.hogwarts.models.Student;
import com.example.hogwarts.repository.StudentRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);


    @Override
    public Student addStudent(Student student) {
        logger.info("was invoked method for create new student");
        return studentRepository.save(student);
    }

   public List<Student> getStudentsByName(String name){
        return studentRepository.getStudentsByName(name);
   }


    @Override
    public Student findStudent(long id) {
        logger.info("was invoked method to find some student");
        validateId(id);
        return studentRepository.findById(id).get();
    }

    @Override
    public List<Student> getAllStudents() {
        logger.info("was invoked method for searching all of students in university");
        return studentRepository.findAll();
    }

    // STREAM-API
    @Override
    public List<String> findAllStudentsWhichNameStarts(String letter){
        return studentRepository.findAll()
                .stream()//запуск потока
                .map(Student::getName) //преобразуем каждый объект студента в его имя
                .sorted()
                .filter(i-> i.startsWith(letter.toUpperCase())) //фильтрация имён по первой букве
                .map(String::toUpperCase) //Преобразуем каждое имя в верхний регистр
                .collect(Collectors.toList());//собираем поток в список строк
    }

    @Override
    public Integer getAverageAgeStudents(){
       return studentRepository.findAll()
               .stream()
               .map(Student::getAge)
               .reduce(0,Integer::sum)/studentRepository.findAll().size();
    }
    //

    @Override
    public List<Student> getStudentsByAge(int age) {
        logger.info("was invoked method for searching students by their age");
        validateAge(age);
        return studentRepository.findByAge(age);
    }

    @Override
    public Student changeStudent(Student student) {
        logger.info("was invoked method for change some data of students");
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(long id) {
        logger.info("was invoked method for delete some student");
        validateId(id);
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> findStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("was invoked method to find some students between some age");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }


    private void validateId(long id) {
        if (studentRepository.findById(id).isEmpty()) {
            logger.error("There is not student with id = " + id);
            throw new NotFoundException("Студент с id = " + id + " не существует");
        }
    }

    private void validateAge(int age) {
        if (studentRepository.findByAge(age).isEmpty()) {
            logger.error("There is not student with age = " + age);
            throw new NotFoundException("Студента с возрастом - " + age + " не существует");
        }
    }

    @Override
    public Integer getTotalNumberOfStudents() {
        logger.info("was invoked method to get total number of students");
        return studentRepository.getTotalNumberOfStudents();
    }

    @Override
    public Double getAvgAgeOfStudents(){
        logger.info("was invoked method to get total average age of students");
        return studentRepository.getAvgAgeOfStudents();
    }

    @Override
    public List <Student> getLast5Students(){
        logger.info("was invoked method to get last 5 students");
        return studentRepository.getLast5Students();
    }
}
