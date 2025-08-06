package com.beratcan.first_steps_on_kron.controller;

import com.beratcan.first_steps_on_kron.dto.LessonDto;
import com.beratcan.first_steps_on_kron.dto.StudentDto;
import com.beratcan.first_steps_on_kron.exception.ResourceNotFoundException;
import com.beratcan.first_steps_on_kron.model.Lesson;
import com.beratcan.first_steps_on_kron.model.Semester;
import com.beratcan.first_steps_on_kron.model.Student;
import com.beratcan.first_steps_on_kron.service.EnrollmentService;
import com.beratcan.first_steps_on_kron.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/enrollments")
@AllArgsConstructor
public class EnrollmentController {

    private EnrollmentService enrollmentService;
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<String> enroll(@RequestParam UUID studentId,
                                         @RequestParam String lessonId,
                                         @RequestParam int year,
                                         @RequestParam Semester semester) throws ResourceNotFoundException {
        Student student = studentService.getStudentById(studentId);
        Lesson lesson =  enrollmentService.findLessonById(lessonId);

        enrollmentService.enrollStudent(student, lesson, year, semester);
        return ResponseEntity.ok("Enrolled successfully.");
    }
    @DeleteMapping("{studentId}")
    public ResponseEntity<String> removeEnrollment(@RequestParam UUID studentId,
                                                   @RequestParam String lessonId,
                                                   @RequestParam int year,
                                                   @RequestParam Semester semester) throws ResourceNotFoundException {
        Student student = studentService.getStudentById(studentId);
        Lesson lesson = enrollmentService.findLessonById(lessonId);

        enrollmentService.removeEnrollment(student, lesson, year, semester);
        return ResponseEntity.ok("Enrollment removed successfully.");
    }
    @GetMapping("{studentId}")
    public ResponseEntity<StudentDto> getStudentDetails(@PathVariable UUID studentId) throws ResourceNotFoundException {
        StudentDto details = studentService.getStudentDtoById(studentId);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/lessons")
    public List<LessonDto> getLessons() {
        List<Lesson> lessons = enrollmentService.getLessons();
        return enrollmentService.getLessonDtos(lessons);
    }
    @PostMapping("/lessons")
    public ResponseEntity<String> addLesson(@RequestBody Lesson lesson) {
        enrollmentService.addLesson(lesson);
        return ResponseEntity.ok("Lesson added successfully.");
    }
    @DeleteMapping("/lessons/{lessonId}")
    public ResponseEntity<String> deleteLesson(@PathVariable String lessonId) {
        enrollmentService.deleteLesson(lessonId);
        return ResponseEntity.ok("Lesson deleted successfully.");
    }

}
