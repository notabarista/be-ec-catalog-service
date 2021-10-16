package org.notabarista.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private String id;

    @NotBlank
    private String itemID;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @Min(0)
    @Max(5)
    private Double itemRating;

    protected LocalDateTime createdAt;

    private String userIdentifier;
    private String userFullName;

}
