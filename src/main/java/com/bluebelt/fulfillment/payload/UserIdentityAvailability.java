package com.bluebelt.fulfillment.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/**
 * Kiểm tra người dùng có sẵn không
 */
public class UserIdentityAvailability {

    private Boolean available;

}
