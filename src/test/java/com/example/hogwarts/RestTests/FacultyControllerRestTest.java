package com.example.hogwarts.RestTests;

import com.example.hogwarts.controller.FacultyController;
import com.example.hogwarts.controller.StudentController;
import com.example.hogwarts.models.Faculty;
import com.example.hogwarts.models.Student;
import com.example.hogwarts.repository.FacultyRepository;
import org.assertj.core.api.Assertions;
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

import java.util.Collection;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerRestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    private FacultyRepository facultyRepository;



    @Test
    public void testAddFaculty() throws Exception {//тест Create запроса
        //Добавляем новый факультет и задаём поля
        Faculty faculty = new Faculty();
        faculty.setId(1l);
        faculty.setName("Gryffindor");
        faculty.setColor("red");
        HttpEntity<Faculty> requestEntity = new HttpEntity<>(faculty);
        //проверяем успешность добавления факультета
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        Assertions
                .assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void testUpdateFaculty() throws Exception {//тест Update запроса
        //Добавляем новый факультет и задаём поля
        Long facyltyId = 1L;
        Faculty updatedfaculty = new Faculty();
        updatedfaculty.setId(facyltyId);
        updatedfaculty.setName("Gryffindor");
        updatedfaculty.setColor("red");
        HttpEntity<Faculty> request = new HttpEntity<>(updatedfaculty);

        ResponseEntity<Faculty> createResponse = restTemplate.exchange("http://localhost:"+port+"/faculty",HttpMethod.PUT,request,Faculty.class);
        Assertions.assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK); //проверяем что статус после изменения 200 ОК успешно

    }


    @Test
    public void testFindFaculty() throws Exception { //тест Read запроса
        //Создаём экземпляр факультета
        Long facultyId = 1L;
        Faculty faculty = new Faculty();
        faculty.setId(1l);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty"+ facultyId, Faculty.class);

        Assertions.assertThat(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testDeleteFaculty() throws Exception {//тест Delete запроса
        //Создаём экземпляр факультета
        Faculty faculty = new Faculty();
        faculty.setId(1l);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        Long facultyId = createResponse.getBody().getId();
        //Выполняем DELETE запрос
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/faculty/{id}", HttpMethod.DELETE, null, Void.class, facultyId);
        // Проверка статуса ответа, ответ должен быть (NOT_FOUND) так как запись о студенте удалена
        Assertions
                .assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void testGetAllFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1l);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        ResponseEntity<List<Faculty>> response = restTemplate.exchange("/faculty", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Faculty>>() {
                });
        List<Faculty> faculties = response.getBody();
        Assertions.assertThat(faculties.size()==1); //проверяем что размер листа студентов именно 1
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful());
    }
}
