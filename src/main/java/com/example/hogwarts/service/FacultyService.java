package com.example.hogwarts.service;

import com.example.hogwarts.models.Faculty;
import java.util.List;

public interface FacultyService {

    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(long id);

    List<Faculty> getAllFaculties();

    List<Faculty> getFacultiesByColor(String color);

    Faculty changeFaculty(Faculty faculty);

    void deleteFaculty(long id);

    Faculty findFacultyByColorOrName(String color, String name);

    public String getLongestFacultyName();

    public Integer getStreamParallelAmount();

}
