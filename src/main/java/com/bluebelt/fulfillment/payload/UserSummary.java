package com.bluebelt.fulfillment.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * tóm tắt người dùng
 */
public class UserSummary {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;

}
