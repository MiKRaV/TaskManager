package com.mikrav.taskmanager.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Registration request")
public class RegisterRequest {

    @Schema(description = "Username", example = "Peter")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Email", example = "spiderman@marvel.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should have format user@example.com")
    private String email;

    @Schema(description = "Passowrd", example = "super-hero")
    @NotBlank(message = "Password is required")
    private String password;
}
