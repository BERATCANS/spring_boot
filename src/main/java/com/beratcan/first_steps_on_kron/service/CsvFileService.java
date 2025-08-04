package com.beratcan.first_steps_on_kron.service;

import com.beratcan.first_steps_on_kron.model.CsvFile;
import com.beratcan.first_steps_on_kron.model.Student;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.beratcan.first_steps_on_kron.Repository.StudentRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
@AllArgsConstructor
@Service
public class CsvFileService {
    private StudentRepository repository;

    @Transactional
    public void readStudentsFromCsv(Path path, CsvFile csvFile) {
        StringBuilder errorMessages = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(path)))) {
            br.readLine();
            String line;
            int lineNumber = 1;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                try {
                    String[] record = line.split(";");
                    String name = record[0].trim();
                    String surname = record[1].trim();
                    String number = record[2].trim();
                    Integer num = Integer.valueOf(number);

                    if (name.isEmpty() || surname.isEmpty() || number.isEmpty()) {
                        errorMessages.append("All fields must be filled on line ").append(lineNumber).append(". ");
                    }
                    try {
                        repository.save(Student.builder()
                                .name(name)
                                .surname(surname)
                                .number(num)
                                .accepted(false)
                                .view(true)
                                .build());
                        repository.flush();
                    } catch (DataIntegrityViolationException e) {
                        errorMessages.append("Number already registered: ").append(num)
                                .append(" on line ").append(lineNumber).append(". ");
                        break;
                    }

                } catch (Exception e) {
                    errorMessages.append("Error on line ").append(lineNumber).append(": ").append(e.getMessage()).append(". ");
                    break;
                }
            }

            if (!errorMessages.isEmpty()) {
                csvFile.setIsValid(false);
                csvFile.setErrorMessage(errorMessages.toString());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

        } catch (IOException e) {
            csvFile.setIsValid(false);
            csvFile.setErrorMessage("An error occurred during student data reading.");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }
}
