package org.notabarista.service;

import org.notabarista.controller.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    Page<ReviewDTO> findAll(String itemID, Pageable pageable);

    ReviewDTO addReview(ReviewDTO reviewDTO);

    ReviewDTO updateReview(ReviewDTO reviewDTO);

    void deleteReview(String id);

}
