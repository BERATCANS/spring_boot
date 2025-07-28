package com.beratcan.first_steps_on_kron.controller;
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

    @RequestMapping(method = RequestMethod.GET)
    public List<Student> listBeers(){
        return studentService.getAllStudents();
    }

    @RequestMapping(value = "{studentId}", method = RequestMethod.GET)
    public Student getStudentById(@PathVariable UUID studentId) throws ResourceNotFoundException {
        return studentService.getStudentById(studentId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addStudent(@RequestBody Student student){
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
    public List<Student> searchStudents(@RequestParam("query") String query) {
        return studentService.searchStudents(query);
    }
}
