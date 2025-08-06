package com.beratcan.first_steps_on_kron.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "lessons")
public class Lesson {
    @Column(name = "name")
    private String Name;

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments = new ArrayList<>();
}
