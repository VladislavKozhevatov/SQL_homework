package com.example.hogwarts.RestTests;

import com.example.hogwarts.controller.StudentController;
import com.example.hogwarts.models.Faculty;
import com.example.hogwarts.models.Student;
import com.example.hogwarts.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

//@Nested
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerRestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    private StudentRepository studentRepository;


    @Test
    public void testFindStudent() throws Exception { //тест Read запроса
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student", String.class))
                .isNotNull();
    }

    //создадим вспомогательный метод для создания новых студентов
    public Student createNewStudent(){
        Student student = new Student();
        student.setId(1l);
        student.setAge(20);
        student.setName("Антон");
        return student;
    }

    @Test
    public void testAddStudent() throws Exception {//тест Create запроса
        //Добавляем нового студента и задаём поля
        Student student = new Student();
        createNewStudent();// используем вспомогательный метод добавления новго студента
        //проверяем успешность добавления студента
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class))
                .isNotNull();//проверка на то что что-то приходит
    }

    @Test
    public void testChangeStudent() throws Exception {//тест Update запроса
        //Добавляем нового студента и задаём поля
        Student student = new Student();
        createNewStudent(); // используем вспомогательный метод добавления новго студента
        ResponseEntity<Student> createResponse = restTemplate.postForEntity("/student", student, Student.class);
        Long studentId = createResponse.getBody().getId();
        //Меняем данные студента
        Student updateStudent = new Student();
        student.setId(studentId);
        student.setAge(25);
        student.setName("Игорь");
        HttpEntity<Student> requestEntity = new HttpEntity<>(updateStudent);
        //Выполнение PUT запроса
        ResponseEntity<Student> updateResponse = restTemplate.exchange("/student/{id}", HttpMethod.PUT, requestEntity, Student.class, student);
        Assertions
                .assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);  //проверяем что статус после изменения 200 ОК успешно
    }

    @Test
    public void testDeleteStudent() throws Exception {//тест Delete запроса
        //Добавляем нового студента
        Student student = new Student(); //создаём экземпляр класса
        student.setId(1l);
        student.setAge(20);
        student.setName("Антон");
        ResponseEntity<Student> createResponse = restTemplate.postForEntity("/student", student, Student.class);
        Long studentId = createResponse.getBody().getId();
        //Выполняем DELETE запрос
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/student/{id}", HttpMethod.DELETE, null, Void.class, studentId);
        // Проверка статуса ответа, ответ должен быть (NOT_FOUND) так как запись о студенте удалена
        Assertions
                .assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetAllStudents() throws Exception {//тест Update запроса
        createNewStudent();
        ResponseEntity<List<Student>> response = restTemplate.exchange("/student", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {
                });
        List<Student> students = response.getBody();
        Assertions.assertThat(students.size()==1); //проверяем что размер листа студентов именно 1
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()); //проверяем успешность выполнения get запроса
    }

    @Test
    public void testGetStudentsByAge() throws Exception{
        //Указываем минимальный и максимальный возраст
        int minAge =20;
        int maxAge =25;

        ResponseEntity<List<Student>> response = restTemplate.exchange("http://localhost:" + port + "/student/age-between?minAge=" + minAge + "&maxAge=" + maxAge, HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
        });

        //Проверяем , что статус 200 ОК
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful());//проверяем успешность выполнения get запроса
        List<Student> students = response.getBody();//получаем список студентов
        Assertions.assertThat(students.size()!=0);
        //Проверка на то , что все студенты находятся в заданном диапазоне возрастов
        for (Student student: students){
            Assertions.assertThat(student.getAge()>=minAge && student.getAge()<=maxAge);
        }
    }

}




