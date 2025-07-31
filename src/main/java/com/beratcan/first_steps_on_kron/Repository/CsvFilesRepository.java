package com.beratcan.first_steps_on_kron.Repository;

import com.beratcan.first_steps_on_kron.model.CsvFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CsvFilesRepository extends JpaRepository<CsvFile, Long> {

}
