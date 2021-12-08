package com.bluebelt.fulfillment.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoRequest {

    private String firstName;

    private String lastName;

    private String phone;

}
