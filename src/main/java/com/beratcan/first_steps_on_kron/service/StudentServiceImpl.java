package com.beratcan.first_steps_on_kron.service;

import com.beratcan.first_steps_on_kron.Repository.StudentRepository;
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
        if (student != null) {
            repository.save(student);
        }
    }

    @Override
    public Student getStudentById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Student updateStudent(UUID id, Student updatedStudent) {
        return repository.findById(id)
                .map(student -> {

                    student.setName(updatedStudent.getName());
                    student.setSurname(updatedStudent.getSurname());

                    return repository.save(student);
                })
                .orElse(null);
    }

    // Öğrenci sil
    @Override
    public boolean deleteStudent(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
