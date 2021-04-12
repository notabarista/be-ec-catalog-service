package org.notabarista.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.notabarista.domain.abstracts.AbstractAuditedDocument;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Document("items")
public class Item extends AbstractAuditedDocument {

    @Id
    private String id;

    @NotBlank
    private String name;
    private String description;

    @NotBlank
    private String code;

    @Field("is_active")
    private Boolean isActive;

    private Double rating;

    @NotNull
    private Seller seller;

    private List<String> photos;
    private List<Label> labels;

}
