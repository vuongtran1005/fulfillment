package com.bluebelt.fulfillment.payload.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private String email;
    private List<String> roles;

}
