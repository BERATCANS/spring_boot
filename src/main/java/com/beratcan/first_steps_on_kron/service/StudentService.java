package com.beratcan.first_steps_on_kron.service;

import com.beratcan.first_steps_on_kron.model.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    List<Student> getAllStudents();

    void addStudent(Student student);

    Student getStudentById(UUID id);
}
