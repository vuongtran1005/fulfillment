package com.bluebelt.fulfillment.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
public class Meta {

    @JsonProperty("total_elements")
    public long totalElements;

    @JsonProperty("total_pages")
    public int totalPages;

    @JsonProperty("page")
    public int page;

    @JsonProperty("size")
    public int size;

}
