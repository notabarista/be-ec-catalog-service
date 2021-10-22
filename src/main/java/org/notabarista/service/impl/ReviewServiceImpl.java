package org.notabarista.service.impl;

import lombok.extern.log4j.Log4j2;
import org.notabarista.controller.dto.ReviewDTO;
import org.notabarista.db.ItemRepository;
import org.notabarista.db.ReviewRepository;
import org.notabarista.db.elasticsearch.ReviewESRepository;
import org.notabarista.domain.Review;
import org.notabarista.entity.response.Response;
import org.notabarista.exception.AbstractNotabaristaException;
import org.notabarista.exception.NotFoundException;
import org.notabarista.mappers.ReviewMapper;
import org.notabarista.service.ReviewService;
import org.notabarista.service.util.IBackendRequestService;
import org.notabarista.service.util.enums.MicroService;
import org.notabarista.util.NABConstants;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ItemRepository itemRepository;
    private final ReviewESRepository reviewESRepository;
    private final IBackendRequestService backendRequestService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper, ItemRepository itemRepository, ReviewESRepository reviewESRepository, IBackendRequestService backendRequestService) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.itemRepository = itemRepository;
        this.reviewESRepository = reviewESRepository;
        this.backendRequestService = backendRequestService;
    }

    @Override
    public Page<ReviewDTO> findAll(String itemID, Pageable pageable) {
        log.info("Finding all reviews for item id {} and by page {}", itemID, pageable);

        Page<Review> reviews = reviewRepository.findAllByItemID(itemID, pageable);
        List<ReviewDTO> reviewDTOS = reviewMapper.reviewToDTO(reviews.getContent());
        Set<String> userIDs = reviewDTOS.stream()
                                        .map(ReviewDTO::getUserIdentifier)
                                        .collect(Collectors.toSet());
        Map<String, String> userFullNames = userIDs.stream()
                                                  .collect(Collectors.toMap(userID -> userID, userID -> getUserFullName(userID)));
        reviewDTOS.forEach(reviewDTO -> reviewDTO.setUserFullName(userFullNames.get(reviewDTO.getUserIdentifier())));

        return new PageImpl<>(reviewDTOS, reviews.getPageable(), reviews.getTotalElements());
    }

    @Override
    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        itemRepository.findById(reviewDTO.getItemID()).orElseThrow(() -> new NotFoundException("Item not found"));

        Review review = reviewMapper.dtoToReview(reviewDTO);
        Review savedReview = reviewRepository.insert(review);
        reviewESRepository.save(reviewMapper.reviewToES(savedReview));

        log.info("Added review {}", savedReview);

        return reviewMapper.reviewToDTO(savedReview);
    }

    @Override
    public ReviewDTO updateReview(ReviewDTO reviewDTO) {
        Review existingReview = reviewRepository.findById(reviewDTO.getId()).orElseThrow(() -> new NotFoundException("Review not found"));
        existingReview.setTitle(reviewDTO.getTitle());
        existingReview.setDescription(reviewDTO.getDescription());
        existingReview.setItemRating(reviewDTO.getItemRating());
        existingReview = reviewRepository.save(existingReview);
        reviewESRepository.save(reviewMapper.reviewToES(existingReview));

        log.info("Updated review {}", existingReview);

        return reviewMapper.reviewToDTO(existingReview);
    }

    @Override
    public void deleteReview(String id) {
        boolean exists = reviewRepository.existsById(id);
        reviewRepository.deleteById(id);
        if (exists) {
            reviewESRepository.deleteById(id);
            log.info("Deleted review with id {}", id);
        }
    }

    private String getUserFullName(String userID) {
        Map<String, Object> sellerInfo = null;
        String userFullName = "N/A";
        Response<Map<String, Object>> response = null;
        try {
            log.info("Getting user full name from " + MicroService.USER_MANAGEMENT_SERVICE);
            response = backendRequestService.executeGet(MicroService.USER_MANAGEMENT_SERVICE, "/user/identifier/" + userID,
                    null, new ParameterizedTypeReference<>() {
                    }, Map.of(NABConstants.UID_HEADER_NAME, userID));

            if (response != null && !CollectionUtils.isEmpty(response.getData())) {
                sellerInfo = response.getData().get(0);
                userFullName = sellerInfo.get("firstName") + " " + sellerInfo.get("lastName");
            }
        } catch (AbstractNotabaristaException e) {
            log.error(e);
        }

        return userFullName;
    }

}
