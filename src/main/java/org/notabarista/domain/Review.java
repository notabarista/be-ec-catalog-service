package org.notabarista.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("reviews")
public class Review {

    @Id
    private String id;

    private String itemID;
    private String title;
    private String description;
    private Double itemRating;

    @CreatedDate
    protected LocalDateTime createdAt;

    private String userIdentifier;

}
