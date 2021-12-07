package com.bluebelt.fulfillment.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {

    @NotEmpty(message = "Username must not be empty")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;
    
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 3, max = 30, message = "Password must be between 3 and 30 characters")
    private String password;

    @Size(min = 3, max = 30, message = "First name must be between 3 and 30 characters")
    private String firstName;

    @Size(min = 3, max = 30, message = "Last name must be between 3 and 30 characters")
    private String lastName;

    @Size(min = 10, max = 10, message = "Phone must be 10 characters")
    private String phone;

    private Set<String> roles;

}
