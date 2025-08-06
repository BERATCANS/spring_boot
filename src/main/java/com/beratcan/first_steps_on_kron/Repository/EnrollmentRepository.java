package com.beratcan.first_steps_on_kron.Repository;

import com.beratcan.first_steps_on_kron.model.Enrollment;
import com.beratcan.first_steps_on_kron.model.Lesson;
import com.beratcan.first_steps_on_kron.model.Semester;
import com.beratcan.first_steps_on_kron.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentAndLessonAndYearAndSemester(Student student, Lesson lesson, int year, Semester semester);
    Optional<Enrollment> findByStudentAndLessonAndYearAndSemester(Student student, Lesson lesson, int year, Semester semester);
    List<Enrollment> findAllByStudent(Student student);
}
