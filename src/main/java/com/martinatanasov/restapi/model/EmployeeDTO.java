package com.martinatanasov.restapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmployeeDTO(

        @Schema(description = "ID of the employee", hidden = true, nullable = true)
        Integer id,

        @NotNull(message = "First name is required")
        @Size(min = 3, max = 20, message = "Request between 3 and 20 characters for first name")
        String firstName,

        @Size(min = 3, max = 20, message = "Request between 3 and 20 characters for last name")
        String lastName,

        @NotNull(message = "Email is required")
        @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email")
        String email
) {
}
