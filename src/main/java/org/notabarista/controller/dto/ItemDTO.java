package org.notabarista.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.notabarista.domain.Label;
import org.notabarista.domain.Seller;
import org.notabarista.enums.ItemStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDTO {

    private String id;

    @NotBlank
    private String name;
    private String description;

    @NotBlank
    private String code;

    private ItemStatus status;

    private Double avgRating;
    private Integer nrOfRatings;

    @NotNull
    private Seller seller;

    private Set<String> mediaUrls;
    private List<Label> labels;

    private String createdBy;
    private String modifiedBy;
    private Integer version;

    private List<ReviewDTO> reviews;

}
