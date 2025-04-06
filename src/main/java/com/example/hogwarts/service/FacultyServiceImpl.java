package com.example.hogwarts.service;

import com.example.hogwarts.exeptions.NotFoundException;
import com.example.hogwarts.models.Faculty;
import com.example.hogwarts.repository.FacultyRepository;
import org.apache.commons.lang3.stream.IntStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {

        this.facultyRepository = facultyRepository;
    }

    Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    @Override
    public Faculty addFaculty(Faculty faculty) {
        logger.info("was invoked method for create new faculty");
        return facultyRepository.save(faculty);
    }


    public Faculty findFaculty(long id) {
        logger.info("was invoked method for find some faculty");
        validateId(id);
        return facultyRepository.findById(id).get();
    }

    @Override
    public List<Faculty> getAllFaculties() {
        logger.info("was invoked method to get all faculties");
        return facultyRepository.findAll();
    }

    //STREAM-API
    @Override
    public String getLongestFacultyName(){
        return facultyRepository.findAll()
                .stream()
                .map(Faculty::getName)
                .sorted(Comparator.reverseOrder())
                .findFirst().get();
    }

    public Integer getStreamParallelAmount(){
        long startTime = System.nanoTime();
        int sum = IntStream.iterate(1,a->a+1)
                .parallel()
                .limit(1_000_000)
                .reduce(0, Integer::sum);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("Метод выполнился за" +(duration/1_000_000)+ "мс");
        return sum;
    }

    //
    @Override
    public List<Faculty> getFacultiesByColor(String color) {
        logger.info("was invoked method to get all faculties by color");
        validateColor(color);
        return facultyRepository.findByColor(color);
    }

    @Override
    public Faculty changeFaculty(Faculty faculty) {
        logger.info("was invoked method for change some data faculty");
        return facultyRepository.save(faculty);
    }

    @Override
    public void deleteFaculty(long id) {
        logger.info("was invoked method for delete faculty");
        validateId(id);
        facultyRepository.deleteById(id);
    }

    @Override
    public Faculty findFacultyByColorOrName(String color, String name) {
        logger.info("was invoked method for find some faculty by color");
        return facultyRepository.findByColorIgnoreCaseOrNameContainsIgnoreCase(color, name);
    }

    private void validateId(long id) {
        if (facultyRepository.findById(id).isEmpty()) {
            logger.error("There is not faculty with id = " + id);
            throw new NotFoundException("Факультета с id = " + id + " не существует");
        }
    }

    private void validateColor(String color) {
        if (color == null && color.isBlank()) {
            logger.error("Color is blank");
            throw new IllegalArgumentException();
        }
        if (facultyRepository.findByColor(color).isEmpty()) {
            logger.error("There is no any faculty with this color = " + color);
            throw new NotFoundException("Факультета с цветом - " + color + " не существует");
        }
    }
}
