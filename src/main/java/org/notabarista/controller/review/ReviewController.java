package org.notabarista.controller.review;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.notabarista.controller.dto.ReviewDTO;
import org.notabarista.entity.response.Response;
import org.notabarista.entity.response.ResponseStatus;
import org.notabarista.service.ReviewService;
import org.notabarista.util.NABConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RestController
@CrossOrigin
@RequestMapping("/item/{itemID}/reviews")
@Validated
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<Response<ReviewDTO>> findAll(@PathVariable @NotBlank String itemID, Pageable pageable) {
        StopWatch watch = new StopWatch();
        watch.start();

        Page<ReviewDTO> reviewPage = reviewService.findAll(itemID, pageable);

        watch.stop();

        return new ResponseEntity<>(new Response<>(ResponseStatus.SUCCESS, watch.getTime(), reviewPage.getContent(),
                reviewPage.getTotalElements(), reviewPage.getPageable().getPageNumber(),
                reviewPage.getTotalPages(), reviewPage.getNumberOfElements(), ""), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response<ReviewDTO>> addReview(@NotNull @Valid @RequestBody ReviewDTO reviewDTO, @RequestHeader(NABConstants.UID_HEADER_NAME) String userId) {
        StopWatch watch = new StopWatch();
        watch.start();
        reviewDTO.setUserIdentifier(userId);

        return getItemResponseEntity(reviewService.addReview(reviewDTO), HttpStatus.BAD_REQUEST, watch);
    }

    @PutMapping
    public ResponseEntity<Response<ReviewDTO>> updateReview(@NotNull @Valid @RequestBody ReviewDTO reviewDTO, @RequestHeader(NABConstants.UID_HEADER_NAME) String userId) {
        StopWatch watch = new StopWatch();
        watch.start();
        reviewDTO.setUserIdentifier(userId);

        return getItemResponseEntity(reviewService.updateReview(reviewDTO), HttpStatus.BAD_REQUEST, watch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteById(@PathVariable @NotBlank String id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<Response<ReviewDTO>> getItemResponseEntity(ReviewDTO reviewDTO, HttpStatus httpStatus, StopWatch watch) {
        if (reviewDTO != null) {
            watch.stop();
            List<ReviewDTO> results = List.of(reviewDTO);
            return new ResponseEntity<>(Response.<ReviewDTO>builder()
                                                .status(ResponseStatus.SUCCESS)
                                                .data(results)
                                                .page(1)
                                                .size(results.size())
                                                .totalPageNumber(0)
                                                .total(results.size())
                                                .executionTime(watch.getTime())
                                                .build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(httpStatus);
        }
    }

}
