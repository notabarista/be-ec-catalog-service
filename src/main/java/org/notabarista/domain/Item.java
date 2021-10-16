package org.notabarista.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.notabarista.domain.abstracts.AbstractAuditedDocument;
import org.notabarista.enums.ItemStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {})
@ToString(callSuper = true, exclude = {})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document("items")
public class Item extends AbstractAuditedDocument {

    @Id
    private String id;

    private String name;
    private String description;
    private String code;
    private ItemStatus status;
    private Double avgRating;
    private Integer nrOfRatings;
    private Seller seller;
    private Set<String> mediaUrls;
    private List<Label> labels;

}
