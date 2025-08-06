package com.beratcan.first_steps_on_kron.controller;
import com.beratcan.first_steps_on_kron.dto.StudentDto;
import com.beratcan.first_steps_on_kron.exception.ResourceNotFoundException;
import com.beratcan.first_steps_on_kron.model.Student;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.beratcan.first_steps_on_kron.service.StudentService;

import java.util.List;
import java.util.UUID;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/students")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<StudentDto> listStudents() {
        List<Student> students = studentService.getAllStudents();
        return studentService.getStudentDtos(students);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable UUID studentId) throws ResourceNotFoundException {
        StudentDto dto = studentService.getStudentDtoById(studentId);
        return ResponseEntity.ok(dto);
    }


    @RequestMapping(method = RequestMethod.POST)
    public void addStudent(@RequestBody Student student){
        student.setAccepted(true);
        student.setView(true);
        studentService.addStudent(student);
    }

    @RequestMapping(value = "{studentId}", method = RequestMethod.PUT)
    public ResponseEntity<Student> updateStudent(@PathVariable UUID studentId, @RequestBody Student updatedStudent) throws ResourceNotFoundException {
        Student updated = studentService.updateStudent(studentId, updatedStudent);
        return ResponseEntity.ok(updated);
    }

    @RequestMapping(value = "{studentId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteStudent(@PathVariable("studentId") UUID studentId) throws ResourceNotFoundException {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<StudentDto> searchStudents(@RequestParam("query") String query) {
        List<Student> students = studentService.searchStudents(query);
        return studentService.getStudentDtos(students);
    }
    @RequestMapping(value = "/accepting", method = RequestMethod.GET)
    public List<StudentDto> getAcceptingStudents() {
        List<Student> students = studentService.getAcceptingStudents();
        return studentService.getStudentDtos(students);
    }
    @RequestMapping(value = "{studentId}/accept", method = RequestMethod.PUT)
    public ResponseEntity<Student> acceptStudent(@PathVariable UUID studentId) throws ResourceNotFoundException {
        Student acceptedStudent = studentService.acceptStudent(studentId);
        return ResponseEntity.ok(acceptedStudent);
    }
    @RequestMapping(value = "{studentId}/reject", method = RequestMethod.PUT)
    public ResponseEntity<Student> rejectStudent(@PathVariable UUID studentId) throws ResourceNotFoundException {
        Student declinedStudent = studentService.rejectStudent(studentId);
        return ResponseEntity.ok(declinedStudent);
    }
}
