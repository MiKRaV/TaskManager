package com.mikrav.taskmanager.model.dto;

import com.mikrav.taskmanager.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Registration response")
public class RegisterResponse {

    @Schema(description = "Id", example = "1")
    private Long id;

    @Schema(description = "Username", example = "Peter")
    private String username;

    @Schema(description = "Email", example = "spiderman@marvel.com")
    private String email;

    public static RegisterResponse from(User user) {
        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return response;
    }
}
