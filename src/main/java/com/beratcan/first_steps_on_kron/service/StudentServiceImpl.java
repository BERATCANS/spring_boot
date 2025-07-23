package com.beratcan.first_steps_on_kron.service;

import com.beratcan.first_steps_on_kron.Repository.StudentRepository;
import com.beratcan.first_steps_on_kron.exception.ResourceNotFoundException;
import com.beratcan.first_steps_on_kron.model.Student;
import jakarta.transaction.Transactional;
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
        if (student.getName() != null && student.getSurname() != null) {
            repository.save(student);
        }
        else {
            throw new IllegalArgumentException("Student name and surname cannot be null");
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

        student.setName(updatedStudent.getName());
        student.setSurname(updatedStudent.getSurname());

        return repository.save(student);
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
