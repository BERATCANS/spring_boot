package com.beratcan.first_steps_on_kron.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class Student {
    private String name;
    private String surname;
    private UUID id;
}
