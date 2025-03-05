package com.example.hogwarts.service;

import com.example.hogwarts.exeptions.NotFoundException;
import com.example.hogwarts.models.Faculty;
import com.example.hogwarts.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }


    public Faculty findFaculty(long id) {
        validateId(id);
        return facultyRepository.findById(id).get();
    }

    @Override
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    public List<Faculty> getFacultiesByColor(String color) {
        validateColor(color);
        return facultyRepository.findByColor(color);
    }

    @Override
    public Faculty changeFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public void deleteFaculty(long id) {
        validateId(id);
        facultyRepository.deleteById(id);
    }

    @Override
    public Faculty findFacultyByColorOrName(String color, String name) {
        return facultyRepository.findByColorIgnoreCaseOrNameContainsIgnoreCase(color, name);
    }

    private void validateId(long id) {
        if (facultyRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Факультета с id = " + id + " не существует");
        }
    }

    private void validateColor(String color) {
        if (color == null && color.isBlank()) {
            throw new IllegalArgumentException();
        }
        if (facultyRepository.findByColor(color).isEmpty()) {
            throw new NotFoundException("Факультета с цветом - " + color + " не существует");
        }
    }
}
