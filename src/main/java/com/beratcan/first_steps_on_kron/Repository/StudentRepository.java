package com.beratcan.first_steps_on_kron.Repository;

import com.beratcan.first_steps_on_kron.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByNumber(Integer number);
}
