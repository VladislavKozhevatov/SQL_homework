package com.example.hogwarts.repository;

import com.example.hogwarts.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static org.postgresql.core.SqlCommandType.SELECT;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    @Query(value = "SELECT COUNT(*) FROM public.student", nativeQuery = true)
    Integer getTotalNumberOfStudents();

    @Query(value = "SELECT AVG(age) FROM public.student", nativeQuery = true)
    Double getAvgAgeOfStudents();

//    @Query(value = "SELECT * FROM public.student ORDER BY id DESC LIMIT 5")
//    List<Student> getLast5Students();
}
