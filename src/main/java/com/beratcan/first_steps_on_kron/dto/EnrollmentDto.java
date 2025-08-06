package com.beratcan.first_steps_on_kron.dto;

import com.beratcan.first_steps_on_kron.model.Semester;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {
    private String lessonId;
    private int year;
    private Semester semester;
}
