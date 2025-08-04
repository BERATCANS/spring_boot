package com.beratcan.first_steps_on_kron.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "students")
public class Student {
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "number", unique = true)
    private Integer number;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "view")
    private boolean view;
    @Column(name = "accepted")
    private boolean accepted;

    public Student(String name, String surname, Integer number,boolean accepted, boolean view) {
        this.name = name;
        this.surname = surname;
        this.number = number;
        this.accepted = accepted;
        this.view = view;
    }
}
