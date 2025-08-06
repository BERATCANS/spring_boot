package com.beratcan.first_steps_on_kron.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "csv_files")
public class CsvFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "is_valid")
    private Boolean isValid;
    @Column(name = "message", columnDefinition = "TEXT")
    private String errorMessage;
}
