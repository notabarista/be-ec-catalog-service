package org.notabarista.domain.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "reviews")
public class ReviewES {

    @Id
    private String id;

    private String itemID;
    private String title;
    private String description;
    private Double itemRating;
    protected LocalDateTime createdAt;
    private String userIdentifier;

}
