package org.notabarista.domain.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.notabarista.domain.Label;
import org.notabarista.domain.Seller;
import org.notabarista.enums.ItemStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "items")
public class ItemES {

    @Id
    private String id;

    private String itemId;

    private String name;
    private String description;

    private String code;

    private ItemStatus status;

    private Double avgRating;
    private Integer nrOfRatings;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Seller seller;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Label> labels;

}
