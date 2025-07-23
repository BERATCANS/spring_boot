package com.beratcan.first_steps_on_kron.controller;
import com.beratcan.first_steps_on_kron.model.Student;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.beratcan.first_steps_on_kron.service.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Student> listBeers(){
        return studentService.getAllStudents();
    }
    @RequestMapping(value = "{studentId}", method = RequestMethod.GET)
    public Student getBeerById(@PathVariable("studentId") UUID studentId){
        return studentService.getStudentById(studentId);
    }
    @RequestMapping(method = RequestMethod.POST)
    public void addStudent(Student student){
        studentService.addStudent(student);
    }
    @RequestMapping(value = "{studentId}", method = RequestMethod.PUT)
    public Student updateStudent(@PathVariable("studentId") UUID studentId, Student updatedStudent){
        return studentService.updateStudent(studentId, updatedStudent);
    }
    @RequestMapping(value = "{studentId}", method = RequestMethod.DELETE)
    public boolean deleteStudent(@PathVariable("studentId") UUID studentId) {
        return studentService.deleteStudent(studentId);
    }
}
