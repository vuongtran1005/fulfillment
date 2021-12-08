package com.bluebelt.fulfillment.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * DTO: Data Transfer Object
 */
@Data
public class LoginRequest {

    @NotEmpty(message = "Username or Email must not be empty")
    @Size(min = 3, max = 30, message = "Username or Email must be between 3 and 30 characters")
    private String usernameOrEmail;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 3, max = 30, message = "Password must be between 3 and 30 characters")
    private String password;

}
