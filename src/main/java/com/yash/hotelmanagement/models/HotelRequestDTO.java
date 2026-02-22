package com.yash.hotelmanagement.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HotelRequestDTO {
    @NotBlank(message = "Hotel name is required")
    @Size(max = 100)
    private String name;

    private String imageURL;

    @Min(0)
    @Max(5)
    private Integer rating;
}
