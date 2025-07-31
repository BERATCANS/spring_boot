package com.beratcan.first_steps_on_kron.service;

import com.beratcan.first_steps_on_kron.Repository.CsvFilesRepository;
import com.beratcan.first_steps_on_kron.Repository.StudentRepository;
import com.beratcan.first_steps_on_kron.exception.ResourceNotFoundException;
import com.beratcan.first_steps_on_kron.model.CsvFile;
import com.beratcan.first_steps_on_kron.model.Student;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.slf4j.Logger;
import java.util.stream.Stream;



@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private StudentRepository repository;
    private CsvFilesRepository csvFilesRepository;

    @Override
    public List<Student> getAllStudents() {
        return repository.findAllByViewTrue();
    }

    @Override
    public void addStudent(Student student) {

        if (student.getName() != null && student.getSurname() != null && student.getNumber() != null) {
            Optional<Student> existing = repository.findByNumber(student.getNumber());

            if (existing.isPresent()) {
                throw new IllegalArgumentException("This number is already registered: " + student.getNumber());
            }

            repository.save(student);
        } else {
            throw new IllegalArgumentException("All spaces must be filled!");
        }
    }

    @Override
    public Student getStudentById(UUID id) throws ResourceNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }
    @Override
    public Student updateStudent(UUID id, Student updatedStudent) throws ResourceNotFoundException {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        if (updatedStudent.getName() != null && updatedStudent.getSurname() != null && updatedStudent.getNumber() != null) {

            Optional<Student> existing = repository.findByNumber(updatedStudent.getNumber());

            if (existing.isPresent() && !existing.get().getId().equals(student.getId())) {
                throw new IllegalArgumentException("This number is already registered: " + updatedStudent.getNumber());
            }
            student.setNumber(updatedStudent.getNumber());
            student.setName(updatedStudent.getName());
            student.setSurname(updatedStudent.getSurname());

            return repository.save(student);
        }
        else {
            throw new IllegalArgumentException("All spaces must be filled!");
        }
    }

    @Override
    public boolean deleteStudent(UUID id) throws ResourceNotFoundException {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        repository.deleteById(id);
        return true;
    }
    @Override
    public List<Student> searchStudents(String query) {
        if (query == null || query.isEmpty()) {
            return Collections.emptyList();
        }
        List<Student> result = new ArrayList<>();

        result.addAll(repository.searchByNameOrSurname(query.toLowerCase()));

        try {
            Integer numberQuery = Integer.valueOf(query);
            List<Student> numberMatches = repository.findAllByNumber(query);

            Set<Student> resultSet = new HashSet<>(result);
            resultSet.addAll(numberMatches);
            result = new ArrayList<>(resultSet);
        } catch (NumberFormatException e) {

        }

        return result;
    }
    @Override
    @Transactional
    @Scheduled(fixedRate = 60000)
    public void importCsv() {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        try (Stream<Path> paths = Files.walk(Paths.get("src/main/resources/csvfiles"))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".csv"))
                    .forEach(path -> {
                        CsvFile csvFile = CsvFile.builder().fileName(path.getFileName().toString()).build();
                        boolean success = true;
                        StringBuilder errorMessages = new StringBuilder();

                        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(path)))) {
                            String line;
                            line = br.readLine();
                            String[] record = line.split(";");
                            String name = record[0].trim().toLowerCase();
                            String surname = record[1].trim().toLowerCase();
                            String number = record[2].trim().toLowerCase();

                            if (!("name".equals(name) && "surname".equals(surname) && "number".equals(number))) {
                                errorMessages.append("Headers are not valid. ");
                                throw new Exception("Headers are not valid.");
                            }
                            int a = 0;
                            while ((line = br.readLine()) != null) {
                                record = line.split(";");
                                a++;
                                try {
                                    name = record[0].trim();
                                    surname = record[1].trim();
                                    number = record[2].trim();
                                    Integer num = Integer.valueOf(number);

                                    Student student = new Student(name, surname, num,false,true);
                                    addStudent(student);

                                } catch (IllegalArgumentException e) {
                                    errorMessages.append("Error on line ").append(a).append(": ").append(e.getMessage()).append(". ");
                                    success = false;
                                    break;
                                } catch (Exception e) {
                                    errorMessages.append("Error processing line: ").append(a).append(" ").append(line).append(". ");
                                    success = false;
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            if (errorMessages.isEmpty()) {
                                errorMessages.append("An error occurred during processing. ");
                            }
                            success = false;
                        }
                        if(errorMessages.isEmpty()){
                            errorMessages.append("File processed successfully. ");
                        }
                        csvFile.setIsValid(success);
                        csvFile.setErrorMessage(errorMessages.toString());
                        csvFilesRepository.save(csvFile);

                        try {
                            Path newPath = success ? Paths.get(path.toString() + ".done") : Paths.get(path.toString() + ".fail");
                            Files.move(path, newPath);
                        } catch (IOException e) {
                            logger.error("File could not be renamed: " + path, e);
                        }
                    });
        } catch (IOException e) {
            logger.error("Directory read error: ", e);
        }
    }
    @Override
    public List<Student> getAcceptingStudents() {
        return repository.findAllByAcceptedFalse();
    }
}
