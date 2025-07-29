package com.beratcan.first_steps_on_kron.service;

import com.beratcan.first_steps_on_kron.Repository.StudentRepository;
import com.beratcan.first_steps_on_kron.exception.ResourceNotFoundException;
import com.beratcan.first_steps_on_kron.model.Student;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private StudentRepository repository;

    @Override
    public List<Student> getAllStudents() {
        return repository.findAll();
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

}
