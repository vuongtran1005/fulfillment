package com.bluebelt.fulfillment.payload;

import com.bluebelt.fulfillment.model.user.Info;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {

    private Long id;
    private String username;

    private Info info;
    private String email;
    private Instant joinedAt;


}
