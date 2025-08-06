package com.beratcan.first_steps_on_kron.service;

import com.beratcan.first_steps_on_kron.Repository.EnrollmentRepository;
import com.beratcan.first_steps_on_kron.Repository.LessonRepository;
import com.beratcan.first_steps_on_kron.dto.LessonDto;
import com.beratcan.first_steps_on_kron.dto.StudentDto;
import com.beratcan.first_steps_on_kron.exception.ResourceNotFoundException;
import com.beratcan.first_steps_on_kron.model.Enrollment;
import com.beratcan.first_steps_on_kron.model.Lesson;
import com.beratcan.first_steps_on_kron.model.Semester;
import com.beratcan.first_steps_on_kron.model.Student;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class EnrollmentService {

    private EnrollmentRepository enrollmentRepository;
    private LessonRepository lessonRepository;

    public void enrollStudent(Student student, Lesson lesson, int year, Semester semester) {
        boolean alreadyEnrolled = enrollmentRepository.existsByStudentAndLessonAndYearAndSemester(
                student, lesson, year, semester
        );

        if (alreadyEnrolled) {
            throw new IllegalArgumentException("Student already enrolled in this lesson for the given semester and year.");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .lesson(lesson)
                .year(year)
                .semester(semester)
                .build();
        enrollmentRepository.save(enrollment);
    }
    public Lesson findLessonById(String lessonId) throws ResourceNotFoundException {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));
    }

    public void removeEnrollment(Student student, Lesson lesson, int year, Semester semester) {
        Enrollment enrollment = enrollmentRepository.findByStudentAndLessonAndYearAndSemester(student, lesson, year, semester)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found."));

        enrollmentRepository.delete(enrollment);
    }
    public List<Lesson> getLessons() {
        return lessonRepository.findAll();
    }
    public void addLesson(Lesson lesson) {
        lessonRepository.save(lesson);
    }
    public void deleteLesson(String lessonId) {
        lessonRepository.deleteById(lessonId);
    }
    public List<LessonDto> getLessonDtos(List<Lesson> Lessons) {
        return Lessons.stream()
                .map(lesson -> LessonDto.builder()
                        .id(lesson.getId())
                        .name(lesson.getName())
                        .build())
                .toList();
    }
}

