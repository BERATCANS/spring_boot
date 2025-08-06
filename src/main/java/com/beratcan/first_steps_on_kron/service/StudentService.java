package com.beratcan.first_steps_on_kron.service;

import com.beratcan.first_steps_on_kron.dto.StudentDto;
import com.beratcan.first_steps_on_kron.exception.ResourceNotFoundException;
import com.beratcan.first_steps_on_kron.model.Student;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface StudentService {
    List<Student> getAllStudents();

    void addStudent(Student student);

    Student getStudentById(UUID id) throws ResourceNotFoundException;

    Student updateStudent(UUID id, Student updatedStudent) throws ResourceNotFoundException;

    boolean deleteStudent(UUID id) throws ResourceNotFoundException;
    List<Student> searchStudents(String query);

    @Transactional
    @Scheduled(fixedRate = 60000*5)
    void importCsv();
    List<Student> getAcceptingStudents();
    Student acceptStudent(UUID id) throws ResourceNotFoundException;
    Student rejectStudent(UUID id) throws ResourceNotFoundException;
    StudentDto getStudentDtoById(UUID id) throws ResourceNotFoundException;
    List<StudentDto> getStudentDtos(List<Student> students);
}
