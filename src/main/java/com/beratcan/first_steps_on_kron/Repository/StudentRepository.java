package com.beratcan.first_steps_on_kron.Repository;

import com.beratcan.first_steps_on_kron.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByNumber(Integer number);
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(s.surname) LIKE LOWER(CONCAT('%', :query, '%')) " )
    List<Student> searchByNameOrSurname(@Param("query") String query);

    @Query("SELECT s FROM Student s WHERE CAST(s.number AS string) LIKE %:numberStr%")
    List<Student> findAllByNumber(@Param("numberStr") String numberStr);
    @Query("SELECT s FROM Student s WHERE s.accepted = false AND s.view = true")
    List<Student> findAllByAcceptedFalse();
    @Query("SELECT s FROM Student s WHERE s.accepted = true AND s.view = true")
    List<Student> findAllByViewTrue();
}
