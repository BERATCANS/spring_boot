package com.beratcan.first_steps_on_kron.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDetails {
    private String message;
    private String details;
    private String timestamp;
    private Integer status;
    private String error;
}
