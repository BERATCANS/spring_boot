package com.beratcan.first_steps_on_kron.service;

import com.beratcan.first_steps_on_kron.model.Student;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {
    private Map<UUID, Student> studentMap;
    public StudentServiceImpl() {
        // Initialize the studentMap or any other necessary setup
        this.studentMap = new HashMap<>();
    }
    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentMap.values());
    }

    @Override
    public void addStudent(Student student) {
        if (student != null && student.getId() != null) {
            studentMap.put(student.getId(), student);
        }
    }
    @Override
    public Student getStudentById(UUID id) {
        return studentMap.get(id);
    }
}
