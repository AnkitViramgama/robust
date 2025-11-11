package com.modular.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * User DTO for API responses
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean active;
    private Boolean emailVerified;
    private Set<String> roleIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
