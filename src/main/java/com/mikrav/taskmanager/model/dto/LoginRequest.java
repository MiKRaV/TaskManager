package com.mikrav.taskmanager.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Login request")
public class LoginRequest {

    @Schema(description = "Username", example = "Peter")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Passowrd", example = "super-hero")
    @NotBlank(message = "Password is required")
    private String password;
}
