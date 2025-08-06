package com.beratcan.first_steps_on_kron.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudentDto {
    private UUID id;
    private String name;
    private String surname;
    private Integer number;
    private List<EnrollmentDto> enrollments;
}

