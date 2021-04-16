package org.notabarista.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Seller {

    @NotBlank
    private String userIdentifier;

    private String name;
    private List<String> countries;
    private List<String> cities;

}
