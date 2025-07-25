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
                throw new IllegalArgumentException("Bu numara zaten kayıtlı: " + student.getNumber());
            }

            repository.save(student);
        } else {
            throw new IllegalArgumentException("Tüm alanlar dolu olmalı!");
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

            if (existing.isPresent()) {
                throw new IllegalArgumentException("Bu numara zaten kayıtlı: " + student.getNumber());
            }
            student.setNumber(updatedStudent.getNumber());
            student.setName(updatedStudent.getName());
            student.setSurname(updatedStudent.getSurname());

            return repository.save(student);
        }
        else {
            throw new IllegalArgumentException("Tüm alanlar dolu olmalı!");
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
}
