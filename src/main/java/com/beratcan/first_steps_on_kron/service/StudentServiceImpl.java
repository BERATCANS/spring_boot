package com.beratcan.first_steps_on_kron.service;

import com.beratcan.first_steps_on_kron.Repository.CsvFilesRepository;
import com.beratcan.first_steps_on_kron.Repository.StudentRepository;
import com.beratcan.first_steps_on_kron.exception.ResourceNotFoundException;
import com.beratcan.first_steps_on_kron.model.CsvFile;
import com.beratcan.first_steps_on_kron.model.Student;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import org.slf4j.Logger;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;


@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private StudentRepository repository;
    private CsvFilesRepository csvFilesRepository;
    private final CsvFileService csvFileService;

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
        // Dummy call to avoid unused method warning
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
    @Scheduled(fixedRate = 60000)
    public void importCsv() {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try (Stream<Path> paths = Files.walk(Paths.get("src/main/resources/csvfiles"))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".csv"))
                    .filter(path -> !path.toString().endsWith(".done") && !path.toString().endsWith(".fail"))
                    .forEach(path -> executor.submit(() -> processFile(path)));
        } catch (IOException e) {
            logger.error("Directory read error: ", e);
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
                logger.warn("CSV processing timed out.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("CSV processing interrupted", e);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void processFile(Path path) {
        CsvFile csvFile = CsvFile.builder().fileName(path.getFileName().toString()).build();

        if (isFileValid(path, csvFile)) {
            try {
                csvFileService.readStudentsFromCsv(path, csvFile);
            }
            catch(RuntimeException e){
                csvFile.setIsValid(false);
                csvFile.setErrorMessage(e.getMessage());
            }
            if (csvFile.getIsValid()) {
                csvFile.setErrorMessage("File processed successfully.");
            }
        }
        saveCSVFile(csvFile);
        renameProcessedFile(path, csvFile.getIsValid());
    }

    private boolean isFileValid(Path path, CsvFile csvFile) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(path)))) {
            String line = br.readLine();
            String[] headers = line.split(";");
            csvFile.setIsValid(true);

            if (!("name".equalsIgnoreCase(headers[0].trim())
                    && "surname".equalsIgnoreCase(headers[1].trim())
                    && "number".equalsIgnoreCase(headers[2].trim()))) {
                csvFile.setIsValid(false);
                csvFile.setErrorMessage("Headers are not valid.");
                return false;
            }
        } catch (Exception e) {
            csvFile.setIsValid(false);
            csvFile.setErrorMessage("An error occurred during header validation.");
            return false;
        }
        return true;
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void saveCSVFile(CsvFile csvFile) {
        csvFilesRepository.save(csvFile);
    }


    private void renameProcessedFile(Path path, boolean isSuccess) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        try {
            Path newPath = isSuccess ? Paths.get(path.toString() + ".done") : Paths.get(path.toString() + ".fail");
            Files.move(path, newPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("File could not be renamed: " + path, e);
        }
    }
    @Override
    public List<Student> getAcceptingStudents() {
        return repository.findAllByAcceptedFalse();
    }
    @Override
    public Student acceptStudent(UUID studentId) throws ResourceNotFoundException {
        Student student = repository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        student.setAccepted(true);
        return repository.save(student);
    }
    @Override
    public Student rejectStudent(UUID studentId) throws ResourceNotFoundException {
        Student student = repository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        student.setView(false);
        return repository.save(student);
    }
}
