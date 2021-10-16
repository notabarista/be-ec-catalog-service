package org.notabarista.mappers;

import org.mapstruct.Mapper;
import org.notabarista.controller.dto.ReviewDTO;
import org.notabarista.domain.Review;
import org.notabarista.domain.elasticsearch.ReviewES;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewDTO reviewToDTO(Review review);

    Review dtoToReview(ReviewDTO reviewDTO);

    ReviewES reviewToES(Review review);

    List<ReviewDTO> reviewToDTO(List<Review> reviews);

    List<Review> dtoToReview(List<ReviewDTO> reviewDTOs);

}
